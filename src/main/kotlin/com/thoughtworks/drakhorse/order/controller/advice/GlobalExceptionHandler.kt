package com.thoughtworks.drakhorse.order.controller.advice

import com.thoughtworks.drakhorse.order.exception.InsufficientBalance
import com.thoughtworks.drakhorse.order.exception.OrderCannotCanceled
import com.thoughtworks.drakhorse.order.exception.OrderNotExistsException
import com.thoughtworks.drakhorse.order.exception.PaymentServiceNotAvailable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(val code: String, val message: String);

@RestControllerAdvice
class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(OrderNotExistsException::class)
  fun handleOrderCancellationException(e: OrderNotExistsException): ErrorResponse {
    return ErrorResponse(e.errorCode, e.message)
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(InsufficientBalance::class)
  fun handleOrderCancellationException(e: InsufficientBalance): ErrorResponse {
    return ErrorResponse(e.errorCode, e.message)
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(OrderCannotCanceled::class)
  fun handleOrderCancellationException(e: OrderCannotCanceled): ErrorResponse {
    return ErrorResponse(e.errorCode, e.message)
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(PaymentServiceNotAvailable::class)
  fun handleOrderCancellationException(e: PaymentServiceNotAvailable): ErrorResponse {
    return ErrorResponse(e.errorCode, e.message)
  }
}