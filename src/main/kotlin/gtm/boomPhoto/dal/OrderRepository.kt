package gtm.boomPhoto.dal

import gtm.boomPhoto.models.order.Order
import gtm.boomPhoto.models.order.OrderId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, OrderId>