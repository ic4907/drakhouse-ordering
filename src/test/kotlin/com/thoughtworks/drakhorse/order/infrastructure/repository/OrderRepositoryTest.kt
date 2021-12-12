package com.thoughtworks.drakhorse.order.infrastructure.repository

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.domain.OrderFulfillmentType
import com.thoughtworks.drakhorse.order.domain.OrderStatus
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderEntity
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderFulfillmentEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.ZonedDateTime

internal class OrderRepositoryTest : IntegrationTest() {

  @Autowired
  private lateinit var orderRepository: OrderRepository

  @Autowired
  private lateinit var orderFulfillmentRepository: OrderFulfillmentRepository

  final val orderId = "20211209119918"
  final val orderFulfillmentId = "81018"
  final val orderCanceledFulfillmentId = "9981018"
  val orderEntity = OrderEntity(
      id = orderId,
      userId = "zhangsan",
      merchantId = "haochide",
      price = BigDecimal.valueOf(100),
      status = OrderStatus.WAITING_FOR_PAYMENT,
      orderTime = ZonedDateTime.now().minusMinutes(3)
  )

  val orderFulfillmentEntity = OrderFulfillmentEntity(
      id = orderFulfillmentId,
      orderId = orderId,
      type = OrderFulfillmentType.ORDER_PAYMENT_CONFIRMED
  )

  val orderCanceledFulfillmentEntity = OrderFulfillmentEntity(
      id = orderCanceledFulfillmentId,
      orderId = orderId,
      type = OrderFulfillmentType.CANCELLATION_REQUEST
  )

  @BeforeEach
  override fun setUp() {
    orderRepository.save(orderEntity);
  }

  @Test
  fun should_get_order_success_when_find_by_id() {
    //given
    orderRepository.save(orderEntity);

    // when
    val order = orderRepository.getById(orderId)

    // then
    assertEquals(orderEntity.id, order.id)
    assertEquals(orderEntity.userId, order.userId)
    assertEquals(orderEntity.merchantId, order.merchantId)
    assertEquals(orderEntity.price, order.price)
    assertEquals(orderEntity.orderTime, order.orderTime)
    assertEquals(orderEntity.status, order.status)
  }

  @Test
  fun should_create_order_payment_confirmed_success() {
    // when
    orderFulfillmentRepository.save(orderFulfillmentEntity)
    // then
    val entity = orderFulfillmentRepository.findById(orderFulfillmentId)

    // then
    assertEquals(orderFulfillmentEntity, entity.get())
  }

  @Test
  fun should_create_order_cancel_request_success() {
    // when
    orderFulfillmentRepository.save(orderCanceledFulfillmentEntity)
    // then
    val entity = orderFulfillmentRepository.findById(orderCanceledFulfillmentId)

    // then
    assertEquals(orderCanceledFulfillmentEntity, entity.get())
  }

}