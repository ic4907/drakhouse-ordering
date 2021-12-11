package com.thoughtworks.drakhorse.order.service

import com.thoughtworks.drakhorse.order.domain.OrderStatus
import com.thoughtworks.drakhorse.order.exception.InsufficientBalance
import com.thoughtworks.drakhorse.order.exception.ServerException
import com.thoughtworks.drakhorse.order.infrastructure.client.PaymentClient
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderFulfillmentRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderEntity
import feign.FeignException
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
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
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
  private lateinit var rabbitTemplate: RabbitTemplate

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
    orderService = OrderService(orderRepository, orderFulfillmentRepository, paymentClient, rabbitTemplate)
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
}