package gtm.boomPhoto.dto

import java.time.LocalDateTime
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Constraint(validatedBy = [BusinessHoursValidator::class])
annotation class BusinessHours(
    val message: String = "Time must be within business hours (8:00-20:00) and date must be in the future",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class BusinessHoursValidator : ConstraintValidator<BusinessHours, LocalDateTime?> {
    override fun isValid(value: LocalDateTime?, context: ConstraintValidatorContext?): Boolean =
        value?.let { value.hour in 8..19 && value > LocalDateTime.now() } ?: true
}