package gtm.boomPhoto

import gtm.boomPhoto.common.Json
import gtm.boomPhoto.dal.OrderRepository
import gtm.boomPhoto.dal.PhotographerRepository
import gtm.boomPhoto.models.order.enums.OrderState
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest(
    @Autowired val mockApi: MockMvc,
    @Autowired val orderRepository: OrderRepository,
    @Autowired val photographerRepository: PhotographerRepository,
) {

    @Test
    fun `GET all orders`() {

        val order1 = OrderTestHelper.newOrder(OrderState.PENDING)
        val order2 = OrderTestHelper.newOrder(OrderState.CANCELED)
        val order3 = OrderTestHelper.newOrder(OrderState.UNSCHEDULED)

        val orders = orderRepository.saveAll(listOf(order1, order2, order3))

        mockApi
            .perform(
                MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(orders.size))
            .andDo {
                val result = OrderTestHelper.parseOrders(it)
                assertTrue(result.any { it.orderId == order2.orderId })
            }

    }


    @Test
    fun `POST new order`() {
        val validDateTime = OrderTestHelper.validDateTime()
        val invalidDateTime = OrderTestHelper.invalidDateTime()
        val order1 = OrderTestHelper.newDTOOrder(validDateTime)
        val order2 = OrderTestHelper.newDTOOrder(invalidDateTime)

        mockApi.perform(
            MockMvcRequestBuilders
                .post("/orders")
                .content(Json.gson.toJson(order1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.title == order1.title)
            }


        mockApi.perform(
            MockMvcRequestBuilders
                .post("/orders")
                .content(Json.gson.toJson(order2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andDo {
                assertNotNull(it.resolvedException?.message)
            }
    }

    @Test
    fun `PUT schedule order`() {
        val order1 = OrderTestHelper.newOrder(OrderState.UNSCHEDULED)
        val order2 = OrderTestHelper.newOrder(OrderState.PENDING)
        val orders = orderRepository.saveAll(listOf(order1, order2))
        val validDateTime = OrderTestHelper.validDateTime()

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order1.orderId.id}/schedule")
                .content(Json.gson.toJson(validDateTime))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.PENDING)
                assertNotNull(result.dateTime)
            }


        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order2.orderId.id}/schedule")
                .content(Json.gson.toJson(validDateTime))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${UUID.randomUUID()}/schedule")
                .content(Json.gson.toJson(validDateTime))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)

    }

    @Test
    fun `PUT assign order`() {
        val order1 = OrderTestHelper.newOrder(OrderState.PENDING)
        val order2 = OrderTestHelper.newOrder(OrderState.ASSIGNED)
        val photographer1 = OrderTestHelper.newPhotographer()

        val orders = orderRepository.saveAll(listOf(order1, order2))
        val photographer = photographerRepository.save(photographer1)

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order1.orderId.id}/assign")
                .content(Json.gson.toJson(photographer.photographerId.id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.ASSIGNED)
                assertNotNull(result.photographer)
            }

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order2.orderId.id}/assign")
                .content(Json.gson.toJson(photographer.photographerId.id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)

    }

    @Test
    fun `PUT upload order`() {
        val order1 = OrderTestHelper.newOrder(OrderState.ASSIGNED)
        val zipDTO = OrderTestHelper.newZipDTO()
        val order = orderRepository.save(order1)

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order1.orderId.id}/upload")
                .content(Json.gson.toJson(zipDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.UPLOADED)
            }

    }

    @Test
    fun `PUT verify order`() {
        val order1 = OrderTestHelper.newOrder(OrderState.UPLOADED)
        val order2 = OrderTestHelper.newOrder(OrderState.UPLOADED)
        val orders = orderRepository.saveAll(listOf(order1, order2))

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order1.orderId.id}/verify")
                .content(Json.gson.toJson(true))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.COMPLETED)
            }

        mockApi.perform(
            MockMvcRequestBuilders
                .put("/orders/${order2.orderId.id}/verify")
                .content(Json.gson.toJson(false))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.ASSIGNED)
            }

    }


    @Test
    fun `DELETE cancel order`() {
        val order1 = OrderTestHelper.newOrder(OrderState.UPLOADED)
        val order = orderRepository.save(order1)

        mockApi.perform(
            MockMvcRequestBuilders
                .delete("/orders/${order1.orderId.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parseOrder(it)
                assertTrue(result.state == OrderState.CANCELED)
            }


    }


}
