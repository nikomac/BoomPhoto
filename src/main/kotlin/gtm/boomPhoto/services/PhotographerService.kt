package gtm.boomPhoto.services

import gtm.boomPhoto.dal.OrderRepository
import gtm.boomPhoto.dal.PhotographerRepository
import gtm.boomPhoto.models.contact.Contact
import gtm.boomPhoto.models.contact.ContactId
import gtm.boomPhoto.models.order.*
import gtm.boomPhoto.models.photographer.Photographer
import gtm.boomPhoto.models.photographer.PhotographerId
import gtm.boomPhoto.models.photographer.PhotographerNotFoundException
import gtm.boomPhoto.services.interfaces.IOrderService
import gtm.boomPhoto.services.interfaces.IPhotographerService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class PhotographerService(
    private val photographerRepository: PhotographerRepository,
) : IPhotographerService {

    override fun listPhotographers(): List<Photographer> =
        photographerRepository.findAll()

    override fun getPhotographer(photographerId: PhotographerId): Photographer = try {
        photographerRepository.getById(photographerId)
    } catch (e: EntityNotFoundException) {
        throw PhotographerNotFoundException(photographerId)
    }

    override fun createPhotographer(photographer: Photographer): Photographer {
        return photographerRepository.save(photographer)
    }


}