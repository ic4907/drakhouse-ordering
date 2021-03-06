package com.thoughtworks.drakhorse.order.service

import com.thoughtworks.drakhorse.order.domain.OrderFulfillmentType
import com.thoughtworks.drakhorse.order.domain.OrderStatus
import com.thoughtworks.drakhorse.order.exception.InsufficientBalance
import com.thoughtworks.drakhorse.order.exception.OrderCannotCanceled
import com.thoughtworks.drakhorse.order.exception.OrderNotExistsException
import com.thoughtworks.drakhorse.order.infrastructure.client.PaymentClient
import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import com.thoughtworks.drakhorse.order.infrastructure.producer.MessageProducer
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderFulfillmentRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderRepository
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderEntity
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderFulfillmentEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Component
class OrderService(
    private val orderRepository: OrderRepository,
    val orderFulfillmentRepository: OrderFulfillmentRepository,
    private val paymentClient: PaymentClient,
    private val messageProducer: MessageProducer
) {

  /**
   * TODO 在service层，由于时间问题，未能解决掉kotlin新版本与mapstruct存在部分兼容问题
   * TODO 因此实现的时候，service层使用了entity相关的模型，如果时间充分的话，service层应该使用domain模型
   */
  @Transactional
  fun confirmOrderPayment(orderId: String): Boolean {
    val orderOption = orderRepository.findById(orderId)
    if (!orderOption.isPresent) {
      throw OrderNotExistsException()
    }
    val order = orderOption.get()
    val paymentResult = paymentClient.payment(TransactionRequest(order.userId, order.merchantId, order.price))
    if ("SUCCESS" != paymentResult) {
      throw InsufficientBalance()
    }
    val orderFulfillmentEntity = OrderFulfillmentEntity(orderId = orderId, type = OrderFulfillmentType.ORDER_PAYMENT_CONFIRMED)
    createOrderPaymentConfirmedFulfillmentRecord(order, orderFulfillmentEntity)

    return true
  }

  @Transactional
  fun cancelOrder(orderId: String): Boolean {
    val orderOption = orderRepository.findById(orderId)
    if (!orderOption.isPresent) {
      throw OrderNotExistsException()
    }

    val order = orderOption.get()
    if (ZonedDateTime.now().minusMinutes(5).isAfter(order.orderTime)) {
      throw OrderCannotCanceled()
    }
    messageProducer.sendMessage(TransactionRequest(order.merchantId, order.userId, order.price))
    val orderFulfillmentEntity = OrderFulfillmentEntity(orderId = orderId, type = OrderFulfillmentType.CANCELLATION_REQUEST)
    createOrderCancellationRequestFulfillmentRecord(order, orderFulfillmentEntity)

    return true
  }

  /**
   * change order status for easy use in query
   */
  private fun createOrderPaymentConfirmedFulfillmentRecord(orderEntity: OrderEntity, orderFulfillmentEntity: OrderFulfillmentEntity) {
    orderEntity.status = OrderStatus.PAYMENT_CONFIRMED
    orderRepository.save(orderEntity)
    orderFulfillmentRepository.save(orderFulfillmentEntity)
  }

  /**
   * change order status for easy use in query
   */
  private fun createOrderCancellationRequestFulfillmentRecord(order: OrderEntity, orderFulfillmentEntity: OrderFulfillmentEntity) {
    order.status = OrderStatus.CANCELLATION
    orderRepository.save(order)
    orderFulfillmentRepository.save(orderFulfillmentEntity)
  }

}