package com.thoughtworks.drakhorse.order.controller

import com.thoughtworks.drakhorse.order.controller.response.CancelOrderResponse
import com.thoughtworks.drakhorse.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{id}/payment-confirmation")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @PathVariable id: String
    ): String {
        val paymentSuccess = orderService.confirmOrderPayment(id)
        if (paymentSuccess) {
            return "Success"
        }
        return "UNKNOWN_ERROR"
    }

    @PostMapping("/{id}/refund-request")
    fun cancelOrder(@PathVariable("id") id: Long): CancelOrderResponse {
        val cancelOrder: Boolean = orderService.cancelOrder(id)
        return CancelOrderResponse()
    }

}