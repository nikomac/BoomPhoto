package gtm.boomPhoto.models.order

import gtm.boomPhoto.models.contact.Contact
import gtm.boomPhoto.models.order.enums.OrderState
import gtm.boomPhoto.models.order.enums.PhotoType
import gtm.boomPhoto.models.photographer.Photographer
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @EmbeddedId
    val orderId: OrderId,
    val photoType: PhotoType,
    val title: String?,
    val logisticsInfo: String?,
    val dateTime: LocalDateTime?,
    val state: OrderState,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    val contact: Contact,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    val photographer: Photographer? = null
) {

    fun assertIsAtState(orderState: OrderState) =
        also { if (state != orderState) throw InvalidOrderStateException(orderId, orderState, state) }

    fun assertIsNotAtState(orderState: OrderState) =
        also { if (state == orderState) throw InvalidOrderStateException(orderId, orderState, state) }

    fun setNewOrderState() : Order =
        dateTime?.let { copy(state = OrderState.PENDING) } ?: copy(state = OrderState.UNSCHEDULED)

    fun scheduleDateTime(schedule: LocalDateTime) : Order =
        copy(dateTime = schedule, state = OrderState.PENDING)

    fun assignPhotographer(photographer: Photographer) : Order =
        copy(photographer = photographer, state = OrderState.ASSIGNED)

    fun uploadPhotos() : Order =
        copy(state = OrderState.UPLOADED)

    fun verifyPhotos(isAccepted: Boolean) : Order =
        copy(state = if(isAccepted) OrderState.COMPLETED else OrderState.ASSIGNED)

    fun cancelOrder() : Order =
        copy(state = OrderState.CANCELED)


    companion object {
        fun create(
            photoType: PhotoType,
            title: String?,
            logisticsInfo: String?,
            dateTime: LocalDateTime?,
            contact: Contact,
        ) = Order(
            orderId = OrderId(),
            photoType = photoType,
            title = title,
            logisticsInfo = logisticsInfo,
            dateTime = dateTime,
            contact = contact,
            state = OrderState.PENDING,
        )
    }

}