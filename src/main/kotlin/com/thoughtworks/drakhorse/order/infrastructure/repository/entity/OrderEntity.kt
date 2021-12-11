package com.thoughtworks.drakhorse.order.infrastructure.repository.entity

import com.thoughtworks.drakhorse.order.domain.OrderStatus
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val merchantId: String,
    val price: BigDecimal,
    val orderTime: ZonedDateTime,
    var status: OrderStatus
)