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
import brasajava.person.domain.utils.AuditableUtils;
import brasajava.person.domain.utils.IdentifiableUtils;
import brasajava.person.message.event.AddressDeleteEvent;
import brasajava.person.message.event.ContactDeleteEvent;
import brasajava.person.message.event.PersonActivateEvent;
import brasajava.person.message.event.PersonCreateEvent;
import brasajava.person.message.event.PersonUpdateEvent;
import brasajava.person.message.event.PersonalDocumentDeleteEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

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
	
	public void setActive(Boolean active) {
		this.active = active;
		if(active) activatePerson();
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
	private Person createPerson() {
		IdentifiableUtils.identify(Person.class, this);
		registerEvent(new PersonCreateEvent(id));
		return this;
	}
	
	private Person activatePerson() {
		registerEvent(new PersonActivateEvent(id));
		return this;
	}

	private Person updatePerson() {
		registerEvent(new PersonUpdateEvent(id));
		return this;
	}
	
	/* --------------------------- person events ---------------------------*/
	
	
	/* ===================================== Delete Document =======================================*/
	
	private Person deleteDocument(String documentId) {
		registerEvent(new PersonalDocumentDeleteEvent(documentId, id));
		return this;
	}
	/* ===================================== Delete Document =======================================*/
	
	/* ===================================== Delete Contact =======================================*/
	
	private Person deleteContact(Contact contact) {
		registerEvent(new ContactDeleteEvent(contact.getId(), id, contact));
		return this;
	}
	/* ===================================== Delete Contact =======================================*/
	
	/* ===================================== Delete Address =======================================*/
	
	private Person deleteAddress(String addressId) {
		registerEvent(new AddressDeleteEvent(addressId, id));
		return this;
	}
	/* ===================================== Delete Address =======================================*/
	
		
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
	public Tuple2<Person,PersonalDocument> addDocument(PersonalDocument document, String user) {
		try {
			PersonalDocument clonedDocument = document.clone();
			IdentifiableUtils.identify(PersonalDocument.class, clonedDocument);
			this.documents.add(clonedDocument);
			documentsToUpdate.add(clonedDocument.getId());
			return Tuples.of(this.clone(), clonedDocument.clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Tuple2<Person, PersonalDocument> updateDocument(String documentId, PersonalDocument document, String user) {
		try {
			Optional<PersonalDocument> optionalDocument = documents.stream().filter(d -> d.getId().equals(documentId)).findFirst();
			if(optionalDocument.isPresent()) {
				optionalDocument.get().setPerson(this);
				BeanUtils.copyProperties(document,optionalDocument.get(), "id","auditInfo");
				documentsToUpdate.add(documentId);
				return Tuples.of(this.clone(), optionalDocument.get().clone()) ;				
			}else {
				throw new RuntimeException("Document not found");
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	public Person deleteDocumentById(String documentId, String user){
		Optional<PersonalDocument> optionalDocument = documents.stream().filter(d -> d.getId().equals(documentId)).findFirst();
		if(optionalDocument.isPresent()) {
			if(documents.remove(optionalDocument.get())) {
				try {
					return deleteDocument(documentId).clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e.getCause());
				}
			}else {
				throw new RuntimeException("Not able to remove the document");
			}
		}else {
			throw new RuntimeException("Document not found");
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
	
	public Person deleteContactById(String contactId, String user){
		Optional<Contact> optionalContact = contacts.stream().filter(c -> c.getId().equals(contactId)).findFirst();
		if(optionalContact.isPresent()) {
			if(contacts.remove(optionalContact.get())) {
				try {
					return deleteContact(optionalContact.get()).clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e.getCause());
				}
			}else {
				throw new RuntimeException("Not able to remove the contact");
			}
		}else {
			throw new RuntimeException("Contact not found");
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
	
	public Person deleteAddressById(String addressId, String user){
		Optional<Address> optionalAddress = addresses.stream().filter(a -> a.getId().equals(addressId)).findFirst();
		if(optionalAddress.isPresent()) {
			if(addresses.remove(optionalAddress.get())) {
				try {
					return deleteAddress(addressId).clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e.getCause());
				}
			}else {
				throw new RuntimeException("Not able to remove the address");
			}
		}else {
			throw new RuntimeException("Address not found");
		}
	}
	/* --------------------------- addresses actions ---------------------------*/
	
}
