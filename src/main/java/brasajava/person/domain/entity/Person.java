package brasajava.person.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import brasajava.person.domain.comun.Auditable;
import brasajava.person.domain.comun.Identifiable;
import brasajava.person.domain.event.PersonActivateEvent;
import brasajava.person.domain.event.PersonCreateEvent;
import brasajava.person.domain.event.PersonUpdateEvent;
import brasajava.person.domain.utils.AuditableUtils;
import brasajava.person.domain.utils.IdentifiableUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class Person extends AbstractAggregateRoot<Person> implements Cloneable, Auditable, Identifiable{
	private String id;
	private String name;
	private String firstname;
	private String lastname;
	private LocalDate birthday;
	private List<PersonalDocument> documents;
	private List<Contact> contacts;
	private List<Address> addresses;
	private boolean active;
	private LocalDateTime systemActivationDateTime;
	private AuditInfo auditInfo;
	@Transient @JsonIgnore private Set<String> documentsToUpdate;
	@Transient @JsonIgnore private Set<String> contactsToUpdate;
	@Transient @JsonIgnore private Set<String> addressesToUpdate;
	
	public Person() {
		auditInfo = new AuditInfo();
		documents = new ArrayList<PersonalDocument>();
		documentsToUpdate = new HashSet<>();

		contacts = new ArrayList<Contact>();
		contactsToUpdate = new HashSet<>();
		
		addresses = new ArrayList<Address>();
		addressesToUpdate = new HashSet<>();
		
	}
	
	@Override
	protected Person clone() throws CloneNotSupportedException {
	 	  return Person.class.cast(super.clone());  
	}
	
	
	public Person onCreate(String user) {
		List<Identifiable> identifiableList = collectIdentifiables();
		List<Auditable> auditableList = collectAuditables();
		IdentifiableUtils.identify(identifiableList);
		AuditableUtils.onCreation(auditableList, user);
		return createPerson();
	}

	public Person onUpdate(String user) {
		List<Auditable> auditableList = new ArrayList<>();
		
		auditableList.add(this);
		
		documents.stream()
		.filter(d -> documentsToUpdate.contains(d.getId()))
		.forEach(d -> {
			auditableList.add(d);
			d.getEventList().forEach(e -> registerEvent(e));
			});
		
		contacts.stream()
		.filter(c -> contactsToUpdate.contains(c.getId()))
		.forEach(c -> {
			auditableList.add(c);
			c.getEventList().forEach(e -> registerEvent(e));
			});
		
		addresses.stream()
		.filter(a -> addressesToUpdate.contains(a.getId()))
		.forEach(a -> {
			auditableList.add(a);
			a.getEventList().forEach(e -> registerEvent(e));
		});
		
		AuditableUtils.onUpdate(auditableList, user);
		return this.updatePerson();
	}
	
	private List<Identifiable> collectIdentifiables() {
		List<Identifiable> identifiableList = new ArrayList<>();
		identifiableList.add(this);
		identifiableList.addAll(documents);
		identifiableList.addAll(contacts);
		identifiableList.addAll(addresses);
		return identifiableList;
	}

	private List<Auditable> collectAuditables() {
		List<Auditable> auditableList = new ArrayList<>();
		auditableList.add(this);
		auditableList.addAll(documents);
		auditableList.addAll(contacts);
		auditableList.addAll(addresses);
		return auditableList;
	}
	
		
	/* --------------------------- person events ---------------------------*/
	protected Person createPerson() {
		IdentifiableUtils.identify(Person.class, this);
		registerEvent(new PersonCreateEvent(id));
		return this;
	}
	
	protected Person activatePerson() {
		registerEvent(new PersonActivateEvent(id));
		return this;
	}

	protected Person updatePerson() {
		registerEvent(new PersonUpdateEvent(id));
		return this;
	}
	/* --------------------------- person events ---------------------------*/
	
	
		
	/* --------------------------- documents actions ---------------------------*/
	public List<PersonalDocument> getDocuments(){
		return new ArrayList<>(documents);
	}
	
	public Optional<PersonalDocument> getDocumentById(String id) {
		return documents.stream()
				.filter(d -> d.getId().equals(id))
				.map(d -> {	
					try {
						PersonalDocument clonedDocument = d.clone();
						clonedDocument.setPerson(this.clone());	
						return clonedDocument;
					} catch (CloneNotSupportedException e) {
						throw new RuntimeException(e.getCause());
					}
				})
				.findFirst();
	}
	
	public Person addDocumentList(List<PersonalDocument> documents, String user) {
		try {
			for(PersonalDocument document: documents) {
				PersonalDocument clonedDocument = document.clone();
				IdentifiableUtils.identify(PersonalDocument.class, clonedDocument);
				this.documents.add(clonedDocument);
				documentsToUpdate.add(clonedDocument.getId());
			}
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	public Person addDocument(PersonalDocument document, String user) {
		try {
			PersonalDocument clonedDocument = document.clone();
			IdentifiableUtils.identify(PersonalDocument.class, clonedDocument);
			this.documents.add(clonedDocument);
			documentsToUpdate.add(clonedDocument.getId());
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Person updateDocument(String documentId, PersonalDocument document, String user) {
		try {
			Optional<PersonalDocument> optionalDocument = documents.stream().filter(d -> d.getId().equals(documentId)).findFirst();
			if(optionalDocument.isPresent()) {
				optionalDocument.get().setPerson(this);
				BeanUtils.copyProperties(document,optionalDocument.get(), "id","auditInfo");
				documentsToUpdate.add(documentId);
				return this.clone();				
			}else {
				throw new RuntimeException("Document not found");
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	/* --------------------------- documents actions ---------------------------*/
	
	
	
	/* --------------------------- contacts actions ---------------------------*/
	public List<Contact> getContacts(){
		return new ArrayList<>(contacts);
	}
	
	public Optional<Contact> getContactById(String id) {
		return contacts.stream()
				.filter(c -> c.getId().equals(id))
				.map(c -> {
					try {
						Contact clonedContact = c.clone();
						clonedContact.setPerson(this.clone());
						return clonedContact;
					} catch (CloneNotSupportedException e) {
						throw new RuntimeException(e.getCause());
					}
		}).findFirst();
	}
	public Person addContactList(List<Contact> contacts, String user) {
		try {
			for(Contact contact: contacts) {
				Contact clonedContact = contact.clone();
				IdentifiableUtils.identify(Contact.class, clonedContact);
				this.contacts.add(clonedContact);
				contactsToUpdate.add(clonedContact.getId());
			}
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	public Person addContact(Contact contact, String user) {
		try {
			Contact clonedContact = contact.clone();
			IdentifiableUtils.identify(Contact.class, clonedContact);
			this.contacts.add(clonedContact);
			contactsToUpdate.add(clonedContact.getId());
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Person updateContact(String contactId, Contact contact, String user) {
		try {
			Optional<Contact> optionalContact = contacts.stream().filter(c -> c.getId().equals(contactId)).findFirst();
			if(optionalContact.isPresent()) {
				optionalContact.get().setPerson(this);
				BeanUtils.copyProperties(contact,optionalContact.get(), "id","auditInfo");
				contactsToUpdate.add(contactId);
				return this.clone();				
			}else {
				throw new RuntimeException("Contact not found");
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	/* --------------------------- contacts actions ---------------------------*/
	
	
	
	/* --------------------------- addresses actions ---------------------------*/
	public List<Address> getAddresses(){
		return new ArrayList<>(addresses);
	}
	
	public Optional<Address> getAddressById(String id) {
		return addresses.stream()
				.filter(a -> a.getId().equals(id))
				.map(a -> {
					try {
						Address clonedAddress = a.clone();
						clonedAddress.setPerson(this.clone());
						return clonedAddress;
					} catch (CloneNotSupportedException e) {
						throw new RuntimeException(e.getCause());
					}
			}).findFirst();
	}
	
	public Person addAddressList(List<Address> addresses, String user) {
		try {
			for(Address address: addresses) {
				Address clonedAddress = address.clone();
				IdentifiableUtils.identify(Address.class, clonedAddress);
				this.addresses.add(clonedAddress);
				addressesToUpdate.add(clonedAddress.getId());
			}
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Person addAddress(Address address, String user) {
		try {
			Address clonedAddress = address.clone();
			IdentifiableUtils.identify(Address.class, clonedAddress);
			this.addresses.add(clonedAddress);
			addressesToUpdate.add(clonedAddress.getId());
			return this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Person updateAddress(String addressId, Address address, String user) {
		try {
			Optional<Address> optionalAddress = addresses.stream().filter(a -> a.getId().equals(addressId)).findFirst();
			if(optionalAddress.isPresent()) {
				optionalAddress.get().setPerson(this);
				BeanUtils.copyProperties(address,optionalAddress.get(), "id","auditInfo");
				addressesToUpdate.add(addressId);
				return this.clone();				
			}else {
				throw new RuntimeException("Address not found");
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	/* --------------------------- addresses actions ---------------------------*/
	
}