package brasajava.person.domain.entity;

import java.util.Set;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import brasajava.person.domain.comun.Auditable;
import brasajava.person.domain.comun.Identifiable;
import brasajava.person.message.event.ContactAvailabilityChangeEvent;
import brasajava.person.message.event.ContactChangeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Contact implements Cloneable, Auditable, Identifiable{
	private String id;
	private String type;
	private String contact;
	private boolean inactive;
	private boolean cancelled;
	private boolean principal;
	private AuditInfo auditInfo;
	@Transient @JsonIgnore private Person person;
	@Transient @JsonIgnore private Set<Object> eventList;	
	
	public Contact() {
		auditInfo = new AuditInfo();
	}
	
	@Override
	protected Contact clone() throws CloneNotSupportedException{
		return Contact.class.cast(super.clone());
	}
	
	public void setContact(String contact) {
		if(person != null) {
			if(!this.contact.equals(contact)) {
				this.contact = contact;
				changeContact();
			}
		}else {
			this.contact = contact;
		}
		
	}
	
	public void setInactive(boolean inactive) {
		if(person != null) {
			if(this.inactive != inactive) {
				this.inactive = inactive;
				changeContactAvailability(this.inactive);
			}
		}else {
			this.inactive = inactive;
		}
	}
	
	public void setCancelled(boolean cancelled) {
		if(person != null) {
			if(this.cancelled != cancelled) {
				this.cancelled = cancelled;
				changeContactAvailability(this.cancelled);
			}
		}else {
			this.cancelled = cancelled;
		}
	}
	
	protected Contact changeContact() {
		eventList.add(new ContactChangeEvent(id,person.getId(),contact));
		return this;
	}
	
	protected Contact changeContactAvailability(boolean isAvailable) {
		eventList.add(new ContactAvailabilityChangeEvent(id, person.getId(), isAvailable));
		return this;
	}
}
