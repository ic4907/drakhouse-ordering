package com.thoughtworks.drakhorse.order.infrastructure.repository.entity

import com.thoughtworks.drakhorse.order.domain.OrderFulfillmentType
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "order_fulfillments")
data class OrderFulfillmentEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val orderId: String,
    val type: OrderFulfillmentType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as OrderFulfillmentEntity

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
      return this::class.simpleName + "(orderId = $orderId , type = $type )"
    }
}
