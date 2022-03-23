package gtm.boomPhoto.dal

import gtm.boomPhoto.models.contact.Contact
import gtm.boomPhoto.models.contact.ContactId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, ContactId>