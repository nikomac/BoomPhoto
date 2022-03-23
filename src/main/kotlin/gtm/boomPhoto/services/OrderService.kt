package gtm.boomPhoto.services

import gtm.boomPhoto.dal.OrderRepository
import gtm.boomPhoto.models.order.*
import gtm.boomPhoto.models.order.enums.OrderState
import gtm.boomPhoto.models.photographer.PhotographerId
import gtm.boomPhoto.services.interfaces.IOrderService
import gtm.boomPhoto.services.interfaces.IPhotographerService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val photographerService: IPhotographerService,
) : IOrderService {

    override fun listOrders(): List<Order> =
        orderRepository.findAll()

    override fun getOrder(orderId: OrderId): Order = try {
        orderRepository.getById(orderId)
    } catch (e: EntityNotFoundException) {
        throw OrderNotFoundException(orderId)
    }

    override fun createOrder(order: Order): Order {
        val updatedOrder = order.setNewOrderState()
        return orderRepository.save(updatedOrder)
    }

    override fun scheduleOrder(orderId: OrderId, dateTime: LocalDateTime): Order {
        val order = getOrder(orderId)
            .assertIsAtState(OrderState.UNSCHEDULED)

        val scheduledOrder = order.scheduleDateTime(dateTime)
        return orderRepository.save(scheduledOrder)
    }

    override fun assignOrder(orderId: OrderId, photographerId: PhotographerId): Order {
        val order = getOrder(orderId)
            .assertIsAtState(OrderState.PENDING)

        val photographer = photographerService.getPhotographer(photographerId)
        val assignedOrder = order.assignPhotographer(photographer)
        return orderRepository.save(assignedOrder)
    }

    override fun uploadOrderPhotos(orderId: OrderId): Order {
        val order = getOrder(orderId)
            .assertIsAtState(OrderState.ASSIGNED)

        val uploadedOrder = order.uploadPhotos()
        return orderRepository.save(uploadedOrder)
    }

    override fun verifyOrder(orderId: OrderId, isAccepted: Boolean): Order {
        val order = getOrder(orderId)
            .assertIsAtState(OrderState.UPLOADED)

        val verifiedOrder = order.verifyPhotos(isAccepted)
        return orderRepository.save(verifiedOrder)
    }

    override fun cancelOrder(orderId: OrderId): Order {
        val order = getOrder(orderId)
            .assertIsNotAtState(OrderState.CANCELED)

        val canceledOrder = order.cancelOrder()
        return orderRepository.save(canceledOrder)
    }

}