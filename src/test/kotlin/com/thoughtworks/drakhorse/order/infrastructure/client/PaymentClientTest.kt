package com.thoughtworks.drakhorse.order.infrastructure.client

import com.thoughtworks.drakhorse.order.IntegrationTest
import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

/**
 * 由于时间问题，payment-server.url对应的mock服务代码未能完工，对应的PaymentClientTest需要依赖的mock服务需要预先临时做一个mock-server
 */
@Disabled("由于时间问题，payment-server.url对应的mock服务代码未能完工，对应的PaymentClientTest需要依赖的mock服务需要预先临时做一个mock-server")
class PaymentClientTest : IntegrationTest() {

  @Autowired
  lateinit var paymentClient: PaymentClient

  @Test
  fun should_payment_success_to_payment_server() {
    // given
    val request = TransactionRequest(
        "U1929281",
        "M19291883",
        BigDecimal.valueOf(80)
    )

    // when
    val result = paymentClient.payment(request)

    // then
    assertEquals("SUCCESS", result)
  }

  @Test
  fun should_payment_success_to_payment_server_when_account_balance_equals_order_price() {
    // given
    val request = TransactionRequest(
        "U1929288",
        "M19291883",
        BigDecimal.valueOf(100)
    )

    // when
    val result = paymentClient.payment(request)

    // then
    assertEquals("SUCCESS", result)
  }

  @Test
  fun should_payment_failed_to_payment_server_when_balance_is_insufficient() {
    // given
    val request = TransactionRequest(
        "U1929281",
        "M19291883",
        BigDecimal.valueOf(18000)
    )

    // when
    val result = paymentClient.payment(request)

    // then
    assertEquals("TRANSACTION_FAILED", result)
  }

  @Test
  fun should_payment_failed_to_payment_server_when_server_error() {
    // given
    val request = TransactionRequest(
        "U1929281",
        "M19291883",
        BigDecimal.valueOf(18000)
    )

    // when
    val result = paymentClient.payment(request)

    // then
    assertEquals("TRANSACTION_FAILED", result)
  }
}