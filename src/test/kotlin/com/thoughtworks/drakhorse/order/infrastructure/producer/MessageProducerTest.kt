package com.thoughtworks.drakhorse.order.infrastructure.producer

import com.thoughtworks.drakhorse.order.infrastructure.client.request.TransactionRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class MessageProducerTest {

  @Mock
  private lateinit var rabbitTemplate: RabbitTemplate

  private lateinit var messageProducer: MessageProducer

  @BeforeEach
  fun init() {
//    rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
    messageProducer = MessageProducer(rabbitTemplate)
  }

  @Test
  fun should_send_message_to_rabbitmq_server_success() {
    // given
    val messageBody = TransactionRequest("A", "B", BigDecimal.valueOf(100))
    val captor: ArgumentCaptor<TransactionRequest> = ArgumentCaptor.forClass(TransactionRequest::class.java)

    // when
    messageProducer.sendMessage(messageBody)

    // then
    verify(rabbitTemplate).convertAndSend(captor.capture())
    val message = captor.value
    Assertions.assertEquals(messageBody.from, message.from)
    Assertions.assertEquals(messageBody.to, message.to)
    Assertions.assertEquals(messageBody.amount, message.amount)
  }

}