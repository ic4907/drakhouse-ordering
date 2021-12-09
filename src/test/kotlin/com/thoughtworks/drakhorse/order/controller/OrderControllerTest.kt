package com.thoughtworks.drakhorse.order.controller

import com.thoughtworks.drakhorse.order.IntegrationTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class OrderControllerTest : IntegrationTest() {

    @Test
    fun should_create_order_success() {
        mockMvc.perform(
            post("/orders/food-proposals/1/order")
                .content("{\n  \"shopId\": \"S-1231231\",\n  \"items\": [\n    {\n      \"price\": 12.1,\n      \"quantity\": 1\n    }\n  ]\n}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
    }
}