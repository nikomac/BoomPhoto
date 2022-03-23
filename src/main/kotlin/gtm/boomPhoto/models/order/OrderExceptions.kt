package gtm.boomPhoto.models.order

import gtm.boomPhoto.models.order.enums.OrderState
import javax.persistence.EntityNotFoundException

class OrderNotFoundException(orderId: OrderId) :
    EntityNotFoundException("Order ${orderId.id} not found.")

class InvalidOrderStateException(orderId: OrderId, expectedState: OrderState, currentState: OrderState) :
    Exception("Order state on ${orderId.id} not allowed on this operation, expected state $expectedState, current state $currentState.")