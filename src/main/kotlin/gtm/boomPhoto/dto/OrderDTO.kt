package gtm.boomPhoto.dto

import gtm.boomPhoto.models.order.Order
import gtm.boomPhoto.models.order.enums.PhotoType
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class OrderDTO(
    @field:NotNull(message = "Contact data cannot be null")
    val contact: ContactDTO,
    @field:NotNull(message = "Photo type cannot be null")
    val photoType: PhotoType,
    val title: String?,
    val logisticsInfo: String?,
    @field:BusinessHours
    val dateTime: LocalDateTime?,
) {
    fun toDomain(): Order =
        Order.create(
            photoType = photoType,
            title = title,
            logisticsInfo = logisticsInfo,
            dateTime = dateTime,
            contact = contact.toDomain()
        )
}

