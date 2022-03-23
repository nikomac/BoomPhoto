package gtm.boomPhoto

import gtm.boomPhoto.common.Json
import gtm.boomPhoto.dto.ContactDTO
import gtm.boomPhoto.dto.OrderDTO
import gtm.boomPhoto.dto.PhotographerDTO
import gtm.boomPhoto.dto.ZipDTO
import gtm.boomPhoto.models.contact.Contact
import gtm.boomPhoto.models.contact.ContactId
import gtm.boomPhoto.models.order.Order
import gtm.boomPhoto.models.order.OrderId
import gtm.boomPhoto.models.order.enums.OrderState
import gtm.boomPhoto.models.order.enums.PhotoType
import gtm.boomPhoto.models.photographer.Photographer
import org.springframework.test.web.servlet.MvcResult
import java.time.LocalDateTime
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

class OrderTestHelper {

    companion object {

        fun parseOrder(result: MvcResult): Order =
            Json.gson.fromJson<Order>(result.response.contentAsString, typeOf<Order>().javaType)

        fun parseOrders(result: MvcResult): List<Order> =
            Json.gson.fromJson<List<Order>>(result.response.contentAsString, typeOf<List<Order>>().javaType)

        fun parsePhotographer(result: MvcResult): Photographer =
            Json.gson.fromJson<Photographer>(
                result.response.contentAsString,
                typeOf<Photographer>().javaType
            )

        fun parsePhotographers(result: MvcResult): List<Photographer> =
            Json.gson.fromJson<List<Photographer>>(
                result.response.contentAsString,
                typeOf<List<Photographer>>().javaType
            )

        fun validDateTime(): LocalDateTime = LocalDateTime.now().plusDays(1).withHour(12)
        fun invalidDateTime(): LocalDateTime = LocalDateTime.now().minusDays(1)

        fun newOrder(state: OrderState): Order =
            Order(
                orderId = OrderId(),
                photoType = PhotoType.EVENTS,
                title = "$state order",
                logisticsInfo = "logistics info",
                dateTime = if (state != OrderState.UNSCHEDULED) validDateTime() else null,
                state = state,
                contact = Contact(
                    contactId = ContactId(),
                    name = "name",
                    surname = "surname",
                    email = "email",
                    cellNumber = "cellNumber",
                ),
            )

        fun newDTOOrder(dateTime: LocalDateTime): OrderDTO =
            OrderDTO(
                contact = ContactDTO(
                    name = "name",
                    surname = "surname",
                    email = "email",
                    cellNumber = "cellNumber",
                ),
                photoType = PhotoType.EVENTS,
                title = "new order",
                logisticsInfo = "logistics info",
                dateTime = dateTime,
            )

        fun newPhotographer(): Photographer =
            Photographer.create("name")

        fun newPhotographerDTO(): PhotographerDTO =
            PhotographerDTO("name")

        fun newZipDTO(): ZipDTO =
            ZipDTO("file.zip")

    }

}