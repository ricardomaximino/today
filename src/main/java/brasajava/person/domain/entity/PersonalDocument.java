package brasajava.person.domain.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import brasajava.person.domain.comun.Auditable;
import brasajava.person.domain.comun.Identifiable;
import brasajava.person.message.event.PersonalDocumentCancelEvent;
import brasajava.person.message.event.PersonalDocumentExpiredEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PersonalDocument implements Cloneable, Auditable, Identifiable{
	private String id;
	private String type;
	private String document;
	private LocalDate expirationDate;
	private boolean expired;
	private boolean cancelled;
	private boolean principal;
	private AuditInfo auditInfo;
	@Transient @JsonIgnore private Person person;
	@Transient @JsonIgnore private Set<Object> eventList;
	public PersonalDocument() {
		auditInfo = new AuditInfo();
		eventList = new HashSet<>();
	}
	
	@Override
	protected PersonalDocument clone() throws CloneNotSupportedException {
		return PersonalDocument.class.cast(super.clone());
	}
	
	public void setExpired(boolean expired) {
		if(person != null) {
			if(this.expired == true) {
				throw new RuntimeException("The document is already expired, none can change it!!");
			}else {
				if(expired == true) {
					this.expired = expired;
					expiredDocument();					
				}
			}
		}else {
			this.expired = expired;
		}
	}
	
	public void setCancelled(boolean cancelled) {
		if(person != null) {
			if(this.cancelled == true) {
				throw new RuntimeException("The document is already cancelled, none can change it!!");
			}else {
				if(cancelled == true) {
					this.cancelled = cancelled;
					cancelDocument();
				}
			}
		}else {
			this.cancelled = cancelled;
		}
	}
	
	private PersonalDocument cancelDocument() {
		eventList.add(new PersonalDocumentCancelEvent(id,person.getId()));
		System.out.println("Document Cancel Event");
		return this;
	}
	
	private PersonalDocument expiredDocument() {
		eventList.add(new PersonalDocumentExpiredEvent(id, person.getId()));
		System.out.println("Document Expired Event");
		return this;
	}

}
