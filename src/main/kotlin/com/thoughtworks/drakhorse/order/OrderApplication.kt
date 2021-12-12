package com.thoughtworks.drakhorse.order

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableRabbit
@EnableFeignClients
@SpringBootApplication
class OrderApplication

fun main(args: Array<String>) {
  runApplication<OrderApplication>(*args)
}
