package com.thoughtworks.drakhorse.order.infrastructure.repository

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class OrderRepositoryTest: IntegrationTest() {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    public fun should_get_order() {
        orderRepository.save(Order())

        val orders = orderRepository.findAll()

        assertEquals(1, orders.size)
    }

}