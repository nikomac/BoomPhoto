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
class PhotographerControllerTest(
    @Autowired val mockApi: MockMvc,
    @Autowired val photographerRepository: PhotographerRepository,
) {

    @Test
    fun `GET all photographers`() {
        val photographer1 = OrderTestHelper.newPhotographer()
        val photographer2 = OrderTestHelper.newPhotographer()
        val photographer3 = OrderTestHelper.newPhotographer()


        val photographers = photographerRepository.saveAll(listOf(photographer1, photographer2, photographer3))

        mockApi
            .perform(
                MockMvcRequestBuilders.get("/photographers").accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(photographers.size))
            .andDo {
                val result = OrderTestHelper.parsePhotographers(it)
                assertTrue(result.any { it.photographerId == photographer2.photographerId })
            }

    }


    @Test
    fun `POST new photographer`() {
        val photographer1 = OrderTestHelper.newPhotographerDTO()
        val photographer2 = OrderTestHelper.newPhotographerDTO().copy(name = "")

        mockApi.perform(
            MockMvcRequestBuilders
                .post("/photographers")
                .content(Json.gson.toJson(photographer1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo {
                val result = OrderTestHelper.parsePhotographer(it)
                assertTrue(result.name == photographer1.name)
            }


        mockApi.perform(
            MockMvcRequestBuilders
                .post("/photographers")
                .content(Json.gson.toJson(photographer2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andDo {
                assertNotNull(it.resolvedException?.message)
            }
    }

}
