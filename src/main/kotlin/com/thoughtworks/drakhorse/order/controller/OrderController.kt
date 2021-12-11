package com.thoughtworks.drakhorse.order.controller

import com.thoughtworks.drakhorse.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

  @PostMapping("/{id}/payment-confirmation")
  @ResponseStatus(HttpStatus.CREATED)
  fun confirmOrderPayment(
      @PathVariable id: String
  ): String {
    val paymentSuccess = orderService.confirmOrderPayment(id)
    if (paymentSuccess) {
      return "SUCCESS"
    }
    return "UNKNOWN_ERROR"
  }

  @PostMapping("/{id}/cancellation-request")
  @ResponseStatus(HttpStatus.CREATED)
  fun cancelOrder(@PathVariable("id") id: String): String {
    val cancelOrder: Boolean = orderService.cancelOrder(id)
    if (cancelOrder) {
      return "SUCCESS"
    }
    return "UNKNOWN_ERROR"
  }

}