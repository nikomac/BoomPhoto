package gtm.boomPhoto.models.photographer

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "photographers")
data class Photographer(
    @EmbeddedId
    val photographerId: PhotographerId,
    val name: String,
) {
    companion object {
        fun create(
            name: String,
        ) = Photographer(
            photographerId = PhotographerId(),
            name = name,
        )
    }
}