package gtm.boomPhoto

import gtm.boomPhoto.models.order.InvalidOrderStateException
import gtm.boomPhoto.models.order.enums.OrderState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderTest {

    @Test
    fun `assert order states`() {
        val order = OrderTestHelper.newOrder(OrderState.COMPLETED)

        assertDoesNotThrow { order.assertIsAtState(OrderState.COMPLETED) }
        assertThrows<InvalidOrderStateException> { order.assertIsAtState(OrderState.PENDING) }

        assertDoesNotThrow { order.assertIsNotAtState(OrderState.CANCELED) }
        assertThrows<InvalidOrderStateException> { order.assertIsNotAtState(OrderState.COMPLETED) }
    }

    @Test
    fun `order states`() {
        val order = OrderTestHelper.newOrder(OrderState.CANCELED)
        val photographer = OrderTestHelper.newPhotographer()
        val validDateTime = OrderTestHelper.validDateTime()

        order.setNewOrderState().let {
            assertEquals(it.state, OrderState.PENDING)
        }

        order.copy(dateTime = null).setNewOrderState().let {
            assertEquals(it.state, OrderState.UNSCHEDULED)
        }

        order.scheduleDateTime(validDateTime).let {
            assertEquals(it.state, OrderState.PENDING)
        }

        order.assignPhotographer(photographer).let {
            assertEquals(it.state, OrderState.ASSIGNED)
            assertNotNull(it.photographer)
        }

        order.uploadPhotos().let {
            assertEquals(it.state, OrderState.UPLOADED)
        }

        order.verifyPhotos(true).let {
            assertEquals(it.state, OrderState.COMPLETED)
        }

        order.verifyPhotos(false).let {
            assertEquals(it.state, OrderState.ASSIGNED)
        }

        order.cancelOrder().let {
            assertEquals(it.state, OrderState.CANCELED)
        }



    }



}