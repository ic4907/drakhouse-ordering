package com.thoughtworks.drakhorse.order.infrastructure.repository.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order {
    @Id
    var id: String = UUID.randomUUID().toString()
}