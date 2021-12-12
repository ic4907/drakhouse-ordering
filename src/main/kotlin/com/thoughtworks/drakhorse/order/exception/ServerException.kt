package com.thoughtworks.drakhorse.order.exception

open class ServerException(
    val errorCode: String = "INTERNAL_SERVER_ERROR",
    override val message: String = "server error"
) : Exception(message)

class PaymentServiceNotAvailable : ServerException("SERVICE_NOT_AVAILABLE", "payment service is not available")
class DatabaseCannotConnected : ServerException("DATABASE_CONNECTED_ERROR", "cannot connected to database")