package com.thoughtworks.drakhorse.order.exception

open class BusinessException(
    val errorCode: String,
    override val message: String
) : Exception(message)

class OrderNotExistsException : BusinessException("ORDER_NOT_EXISTS", "order is not exists")
class OrderCannotCanceled : BusinessException("CANCELLATION_FAILED", "cannot cancel order")
class InsufficientBalance : BusinessException("INSUFFICIENT_BALANCE", "transaction abort, for insufficient balance")