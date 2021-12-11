package com.thoughtworks.drakhorse.order.controller

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.exception.OrderNotExistsException
import com.thoughtworks.drakhorse.order.service.OrderService
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

internal class OrderEntityControllerTest : IntegrationTest() {

  @MockBean
  private lateinit var orderService: OrderService

  @Test
  fun should_payment_success() {
    // given
    val orderId = "20211209119918"
    `when`(orderService.confirmOrderPayment(orderId))
        .thenReturn(true)

    mockMvc.perform(
        post("/orders/${orderId}/payment-confirmation")
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isCreated)
        .andExpect(content().string("SUCCESS"))
  }

  @Test
  fun should_return_order_not_exists_when_order_id_not_found_in_db() {
    // given
    val orderId = "20211209119918"
    `when`(orderService.confirmOrderPayment(orderId))
        .thenAnswer { throw OrderNotExistsException() }

    mockMvc.perform(
        post("/orders/${orderId}/payment-confirmation")
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isNotFound)
        .andExpect(jsonPath("$.code", `is`("ORDER_NOT_EXISTS")))
        .andExpect(jsonPath("$.message", `is`("order is not exists")))
  }

  @Test
  fun should_return_error_response_when_() {
    // given
    val orderId = "20211209119918"
    `when`(orderService.confirmOrderPayment(orderId))
        .thenAnswer { throw OrderNotExistsException() }

    mockMvc.perform(
        post("/orders/${orderId}/payment-confirmation")
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andExpect(status().isNotFound)
        .andExpect(jsonPath("$.code", `is`("ORDER_NOT_EXISTS")))
        .andExpect(jsonPath("$.message", `is`("order is not exists")))
  }
}