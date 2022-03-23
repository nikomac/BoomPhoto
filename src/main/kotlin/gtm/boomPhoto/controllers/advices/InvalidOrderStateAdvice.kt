package gtm.boomPhoto.controllers.advices

import gtm.boomPhoto.models.order.InvalidOrderStateException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
internal class InvalidOrderStateAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidOrderStateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun notFoundHandler(ex: InvalidOrderStateException): String =
        ex.message ?: "Order state is not compatible on this operation."
}