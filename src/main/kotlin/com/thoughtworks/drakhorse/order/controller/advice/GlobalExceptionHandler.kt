package com.thoughtworks.drakhorse.order.controller.advice

import com.thoughtworks.drakhorse.order.exception.OrderNotExistsException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotExistsException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleOrderCancellationException(e: OrderNotExistsException): ErrorResponse {
        return ErrorResponse("ORDER_NOT_EXISTS", "订单不存在")
    }
}