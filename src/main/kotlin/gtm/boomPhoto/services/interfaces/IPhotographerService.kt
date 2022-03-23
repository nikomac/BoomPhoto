package gtm.boomPhoto.services.interfaces

import gtm.boomPhoto.models.photographer.Photographer
import gtm.boomPhoto.models.photographer.PhotographerId

interface IPhotographerService {
    fun listPhotographers(): List<Photographer>
    fun getPhotographer(photographerId: PhotographerId): Photographer
    fun createPhotographer(photographer: Photographer): Photographer
}