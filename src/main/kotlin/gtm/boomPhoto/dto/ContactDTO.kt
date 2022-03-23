package gtm.boomPhoto.dto

import gtm.boomPhoto.models.contact.Contact
import javax.validation.constraints.NotBlank


data class ContactDTO(
    @field:NotBlank(message = "Contact name cannot be empty")
    val name: String,
    @field:NotBlank(message = "Contact surname cannot be empty")
    val surname: String,
    @field:NotBlank(message = "Contact email cannot be empty")
    val email: String,
    @field:NotBlank(message = "Contact cell number cannot be empty")
    val cellNumber: String,
) {

    fun toDomain(): Contact =
        Contact.create(
            name = name,
            surname = surname,
            email = email,
            cellNumber = cellNumber,
        )


}