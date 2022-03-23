package gtm.boomPhoto.models.contact

import gtm.boomPhoto.models.common.Id
import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class ContactId(override val id: UUID = Id.newId()) : Id