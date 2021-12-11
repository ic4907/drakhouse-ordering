package com.thoughtworks.drakhorse.order

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class IntegrationTest {

  protected lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var context: WebApplicationContext

  @BeforeEach
  fun setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
  }
}