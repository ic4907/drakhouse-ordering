package com.thoughtworks.drakhorse.order.service

import com.thoughtworks.drakhorse.order.domain.OrderStatus
import com.thoughtworks.drakhorse.order.exception.DatabaseCannotConnected
import com.thoughtworks.drakhorse.order.exception.InsufficientBalance
import com.thoughtworks.drakhorse.order.exception.OrderCannotCanceled
import com.thoughtworks.drakhorse.order.exception.ServerException
import com.thoughtworks.drakhorse.order.infrastructure.client.PaymentClient
import com.thoughtworks.drakhorse.order.infrastructure.producer.MessageProducer
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderFulfillmentRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OrderServiceTest {

  @Mock
  private lateinit var orderRepository: OrderRepository

  @Mock
  private lateinit var orderFulfillmentRepository: OrderFulfillmentRepository

  @Mock
  private lateinit var paymentClient: PaymentClient

  @Mock
  private lateinit var messageProducer: MessageProducer

  private lateinit var orderService: OrderService

  val orderId = "20211209119918"
  val orderEntity = OrderEntity(
      id = "12312313",
      userId = "zhangsan",
      merchantId = "haochide",
      price = BigDecimal.valueOf(100),
      status = OrderStatus.WAITING_FOR_PAYMENT,
      orderTime = ZonedDateTime.now().minusMinutes(3)
  )

  @BeforeEach
  public fun setUp() {
    orderService = OrderService(orderRepository, orderFulfillmentRepository, paymentClient, messageProducer)
  }

  @Test
  fun should_confirm_order_payment_success() {
    //  given
    `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity))
    `when`(paymentClient.payment(any())).thenReturn("SUCCESS")

    // when
    orderService.confirmOrderPayment(orderId)

    // then
    verify(orderFulfillmentRepository, times(1)).save(any())
    verify(orderRepository, times(1)).save(any())
  }


  @Test()
  fun should_throw_insufficient_balance_exception_when_account_balance_insufficient() {
    //  given

    `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity))
    `when`(paymentClient.payment(any())).thenReturn("INSUFFICIENT_BALANCE")

    // when
    val thrown: InsufficientBalance = Assertions
        .assertThrows(InsufficientBalance::class.java) { orderService.confirmOrderPayment(orderId) }

    // then
    Assertions.assertEquals("INSUFFICIENT_BALANCE", thrown.errorCode)
    Assertions.assertEquals("transaction abort, for insufficient balance", thrown.message)
  }

  @Test()
  fun should_throw_server_exception_when_find_data_in_db_db_is_no_response() {
    //  given
    `when`(orderRepository.findById(orderId)).thenAnswer {
      throw DatabaseCannotConnected()
    }

    // when
    val thrown: DatabaseCannotConnected = Assertions
        .assertThrows(DatabaseCannotConnected::class.java) { orderService.confirmOrderPayment(orderId) }

    // then
    Assertions.assertEquals("DATABASE_CONNECTED_ERROR", thrown.errorCode)
    Assertions.assertEquals("cannot connected to database", thrown.message)
  }

  @Test()
  fun should_throw_exception_when_transfer_service_not_avaliable() {
    //  given
    `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity))
    `when`(paymentClient.payment(any())).thenAnswer {
      throw ServerException()
    }

    // when
    val thrown: ServerException = Assertions
        .assertThrows(ServerException::class.java) { orderService.confirmOrderPayment(orderId) }

    // then
    Assertions.assertEquals("INTERNAL_SERVER_ERROR", thrown.errorCode)
    Assertions.assertEquals("server error", thrown.message)
  }

  @Test
  fun should_cancel_order_success_when_order_can_be_canceled() {
    `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity))

    // when
    val result = orderService.cancelOrder(orderId)

    // then
    Assertions.assertEquals(true, result)
    verify(orderFulfillmentRepository, times(1)).save(any())
    verify(orderRepository, times(1)).save(any())
  }

  @Test
  fun should_cancel_order_failed_when_order_ordering_time_more_then_5m_ago() {
    `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(OrderEntity(
        id = orderId,
        userId = "zhangsan",
        merchantId = "haochide",
        price = BigDecimal.valueOf(100),
        status = OrderStatus.PAYMENT_CONFIRMED,
        orderTime = ZonedDateTime.now().minusMinutes(10)
    )))

    // when
    val thrown: OrderCannotCanceled = Assertions
        .assertThrows(OrderCannotCanceled::class.java) { orderService.cancelOrder(orderId) }

    // then
    Assertions.assertEquals("CANCELLATION_FAILED", thrown.errorCode)
    Assertions.assertEquals("cannot cancel order", thrown.message)
  }
}