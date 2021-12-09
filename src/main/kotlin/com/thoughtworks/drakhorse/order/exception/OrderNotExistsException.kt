package com.thoughtworks.drakhorse.order.exception

import java.lang.RuntimeException

class OrderNotExistsException(message: String?) : RuntimeException(message) {
}