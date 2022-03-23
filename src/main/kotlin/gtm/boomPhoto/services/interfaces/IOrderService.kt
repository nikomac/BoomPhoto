package gtm.boomPhoto.services.interfaces

import gtm.boomPhoto.models.order.Order
import gtm.boomPhoto.models.order.OrderId
import gtm.boomPhoto.models.photographer.PhotographerId
import java.time.LocalDateTime

interface IOrderService {
    fun listOrders(): List<Order>
    fun getOrder(orderId: OrderId): Order
    fun createOrder(order: Order): Order
    fun scheduleOrder(orderId: OrderId, dateTime: LocalDateTime): Order
    fun assignOrder(orderId: OrderId, photographerId: PhotographerId): Order
    fun uploadOrderPhotos(orderId: OrderId): Order
    fun verifyOrder(orderId: OrderId, isAccepted: Boolean): Order
    fun cancelOrder(orderId: OrderId): Order
}