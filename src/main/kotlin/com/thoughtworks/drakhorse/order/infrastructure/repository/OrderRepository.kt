package com.thoughtworks.drakhorse.order.infrastructure.repository

import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, String> {
}