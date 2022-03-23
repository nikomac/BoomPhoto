package gtm.boomPhoto.models.common

import java.io.Serializable
import java.util.*

interface Id : Serializable {
    val id: UUID

    companion object{
        fun newId() = UUID.randomUUID()
    }
}