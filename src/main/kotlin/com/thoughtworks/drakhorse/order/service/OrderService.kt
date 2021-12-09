package com.thoughtworks.drakhorse.order.service

import com.thoughtworks.drakhorse.order.controller.request.ConfirmOrderPaymentRequest
import com.thoughtworks.drakhorse.order.infrastructure.client.PaymentClient
import com.thoughtworks.drakhorse.order.infrastructure.repository.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderService(
    private val orderRepository: OrderRepository,
    private val paymentClient: PaymentClient
) {
    fun cancelOrder(id: Long?): Boolean {
        TODO("Not yet implemented")
    }

    fun confirmOrderPayment(orderId: String): Boolean {
        var order = orderRepository.getById(orderId)

        return true
    }
}