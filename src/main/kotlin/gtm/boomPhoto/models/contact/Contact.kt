package gtm.boomPhoto.models.contact

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "contacts")
data class Contact(
    @EmbeddedId
    val contactId: ContactId,
    val name: String,
    val surname: String,
    val email: String,
    val cellNumber: String,
) {
    companion object {
        fun create(
            name: String,
            surname: String,
            email: String,
            cellNumber: String,
        ) = Contact(
            contactId = ContactId(),
            name = name,
            surname = surname,
            email = email,
            cellNumber = cellNumber,
        )

    }

}