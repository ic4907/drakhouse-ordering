package com.thoughtworks.drakhorse.order.infrastructure.client

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class PaymentClientTest : IntegrationTest() {

  @Autowired
  lateinit var paymentClient: PaymentClient

  @Test
  fun should_payment_success_to_payment_server() {
    // given
    val request = TransactionRequest(
        "zhangsan",
        "chengsile",
        BigDecimal.valueOf(100)
    )

    // when
    var result = paymentClient.payment(request)

    // then
  }
}