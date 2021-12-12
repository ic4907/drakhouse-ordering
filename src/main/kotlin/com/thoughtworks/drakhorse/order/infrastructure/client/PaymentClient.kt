package com.thoughtworks.drakhorse.order.infrastructure.client

import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("transfer.service", url = "\${payment-server.url}")
interface PaymentClient {

  @PostMapping("/transfer/transaction")
  fun payment(@RequestBody transactionRequest: TransactionRequest): String

}
