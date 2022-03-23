package gtm.boomPhoto.controllers

import gtm.boomPhoto.dto.BusinessHours
import gtm.boomPhoto.dto.OrderDTO
import gtm.boomPhoto.dto.ZipDTO
import gtm.boomPhoto.models.order.Order
import gtm.boomPhoto.models.order.OrderId
import gtm.boomPhoto.models.order.OrderNotFoundException
import gtm.boomPhoto.models.photographer.PhotographerId
import gtm.boomPhoto.services.interfaces.IOrderService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@RestController()
@Validated
class OrderController(
    private val orderService: IOrderService
) {
    @GetMapping("/orders")
    fun getOrders(): ResponseEntity<List<Order>> {
        val result = orderService.listOrders()
        return ResponseEntity.ok(result)
    }

    @PostMapping("/orders")
    fun postOrder(@Valid @RequestBody orderDTO: OrderDTO): ResponseEntity<Order> {
        val domainOrder = orderDTO.toDomain()
        val result = orderService.createOrder(domainOrder)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/orders/{orderId}/schedule")
    fun putScheduleOrder(
        @PathVariable orderId: UUID,
        @Valid @RequestBody @BusinessHours schedule: LocalDateTime
    ): ResponseEntity<Order> {
        val id = OrderId(orderId)
        val result = orderService.scheduleOrder(id, schedule)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/orders/{orderId}/assign")
    fun putAssignOrder(
        @PathVariable orderId: UUID,
        @RequestBody photographerId: UUID
    ): ResponseEntity<Order> {
        val oId = OrderId(orderId)
        val pId = PhotographerId(photographerId)
        val result = orderService.assignOrder(oId, pId)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/orders/{orderId}/upload")
    fun putUploadOrder(
        @PathVariable orderId: UUID,
        @RequestBody photoZip: ZipDTO
    ): ResponseEntity<Order> {
        val oId = OrderId(orderId)
        val result = orderService.uploadOrderPhotos(oId)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/orders/{orderId}/verify")
    fun putVerifyOrder(
        @PathVariable orderId: UUID,
        @RequestBody isAccepted: Boolean
    ): ResponseEntity<Order> {
        val oId = OrderId(orderId)
        val result = orderService.verifyOrder(oId, isAccepted)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/orders/{orderId}")
    fun deleteCancelOrder(
        @PathVariable orderId: UUID
    ): ResponseEntity<Order> {
        val oId = OrderId(orderId)
        val result = orderService.cancelOrder(oId)
        return ResponseEntity.ok(result)
    }

}