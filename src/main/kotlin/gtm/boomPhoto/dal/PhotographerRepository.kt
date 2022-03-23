package gtm.boomPhoto.dal

import gtm.boomPhoto.models.photographer.Photographer
import gtm.boomPhoto.models.photographer.PhotographerId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotographerRepository : JpaRepository<Photographer, PhotographerId>
