package com.thoughtworks.drakhorse.order.config

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MessageConfig {

  @Bean
  fun createMessageConverter(): MessageConverter {
    return Jackson2JsonMessageConverter()
  }
}