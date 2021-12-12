package com.thoughtworks.drakhorse.order.infrastructure.producer

import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducer(
    private val rabbitTemplate: RabbitTemplate
) {

  fun sendMessage(request: TransactionRequest) {
    rabbitTemplate.convertAndSend(request)
  }
}