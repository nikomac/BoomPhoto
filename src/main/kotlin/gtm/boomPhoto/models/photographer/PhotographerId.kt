package gtm.boomPhoto.models.photographer

import gtm.boomPhoto.models.common.Id
import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class PhotographerId(override val id: UUID = Id.newId()) : Id