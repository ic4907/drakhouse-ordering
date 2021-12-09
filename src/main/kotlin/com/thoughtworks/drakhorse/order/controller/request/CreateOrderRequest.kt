package com.thoughtworks.drakhorse.order.controller.request

import java.math.BigDecimal
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


class CreateOrderRequest(
    val shopId: String,
    val items: List<OrderItems>
) {
    data class OrderItems(
        @NotNull(message = "订单价格不能为空")
        val price: BigDecimal,
        @Min(1, message = "下单数量最小为1")
        val quantity: Int
    )
}
