package gtm.boomPhoto.controllers.advices

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.persistence.EntityNotFoundException


@ControllerAdvice
internal class NotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundHandler(ex: EntityNotFoundException): String =
        ex.message ?: "Id not found found."
}