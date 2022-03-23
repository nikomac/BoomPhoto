package gtm.boomPhoto.dto

import gtm.boomPhoto.models.photographer.Photographer
import javax.validation.constraints.NotBlank

data class PhotographerDTO(
    @field:NotBlank(message = "Contact name cannot be empty")
    val name: String
){
    fun toDomain(): Photographer =
        Photographer.create(
            name = name,
        )
}