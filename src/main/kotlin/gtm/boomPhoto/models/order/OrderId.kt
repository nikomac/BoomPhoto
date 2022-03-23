package gtm.boomPhoto.models.order

import gtm.boomPhoto.models.common.Id
import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class OrderId(override val id: UUID = Id.newId()) : Id