package com.thoughtworks.drakhorse.order.controller.advice

data class ErrorResponse(
    val code: String,
    val message: String
)