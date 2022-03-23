package gtm.boomPhoto.models.photographer

import javax.persistence.EntityNotFoundException

class PhotographerNotFoundException(photographerId: PhotographerId) :
    EntityNotFoundException("Photographer ${photographerId.id} not found.")