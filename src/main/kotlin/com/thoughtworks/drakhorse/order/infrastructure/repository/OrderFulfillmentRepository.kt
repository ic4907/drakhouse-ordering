package com.thoughtworks.drakhorse.order.infrastructure.repository

import com.thoughtworks.drakhorse.order.infrastructure.repository.entity.OrderFulfillmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderFulfillmentRepository : JpaRepository<OrderFulfillmentEntity, String> {

}
