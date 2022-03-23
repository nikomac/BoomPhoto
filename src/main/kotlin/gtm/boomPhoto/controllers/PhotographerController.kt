package gtm.boomPhoto.controllers

import gtm.boomPhoto.dto.PhotographerDTO
import gtm.boomPhoto.models.photographer.Photographer
import gtm.boomPhoto.services.interfaces.IPhotographerService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@Validated
class PhotographerController(
    private val photographerService: IPhotographerService
) {
    @GetMapping("/photographers")
    fun getPhotographers(): ResponseEntity<List<Photographer>> {
        val result = photographerService.listPhotographers()
        return ResponseEntity.ok(result)
    }

    @PostMapping("/photographers")
    fun postPhotographer(@Valid @RequestBody photographerDTO: PhotographerDTO): ResponseEntity<Photographer> {
        val domainPhotographer = photographerDTO.toDomain()
        val result = photographerService.createPhotographer(domainPhotographer)
        return ResponseEntity.ok(result)
    }

}