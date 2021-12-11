package com.thoughtworks.drakhorse.order.infrastructure.repository

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class OrderEntityRepositoryTest : IntegrationTest() {

  @Autowired
  private lateinit var orderRepository: OrderRepository

  @Test
  fun should_get_order() {
//    orderRepository.save(OrderEntity())
//
//    val orders = orderRepository.findAll()
//
//    assertEquals(1, orders.size)
  }

}