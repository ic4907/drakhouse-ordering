package com.thoughtworks.drakhorse.order.infrastructure.client.request

import java.io.Serializable
import java.math.BigDecimal

data class TransactionRequest(
    val from: String,
    val to: String,
    val amount: BigDecimal
) : Serializable