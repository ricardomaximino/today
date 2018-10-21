package brasajava.person.domain.entity;

import java.util.Set;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import brasajava.person.domain.comun.Auditable;
import brasajava.person.domain.comun.Identifiable;
import brasajava.person.message.event.AddressChangeAvailabilityEvent;
import brasajava.person.message.event.AddressChangeRegionEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Address implements Cloneable, Auditable, Identifiable{
	private String id;
	private String country;
	private String state;
	private String city;
	private String town;
	private String neighborhood;
	private String street;
	private String number;
	private String postCode;
	private String details; 
	private long longitude; 
	private long latitude;
	private boolean inactive;
	private boolean cancelled;
	private boolean principal;
	private AuditInfo auditInfo;
	@Transient @JsonIgnore private Person person;
	@Transient @JsonIgnore private Set<Object> eventList;
	
	public Address() {
		auditInfo = new AuditInfo();
	}
	
	@Override
	protected Address clone() throws CloneNotSupportedException {
		return Address.class.cast(super.clone());
	}
	
	public void setCountry(String country) {
		if(person != null) {
			if(!this.country.equals(country)) {
				this.country = country;
				changeAddressRegion();
			}
		}else {
			this.country = country;
		}
	}
	
	public void setState(String state) {
		if(person != null) {
			if(!this.state.equals(state)) {
				this.state = state;
				changeAddressRegion();
			}
		}else {
			this.state = state;
		}
	}
	
	public void setCity(String city) {
		if(person != null) {
			if(!this.city.equals(city)) {
				this.city = city;
				changeAddressRegion();
			}
		}else {
			this.city = city;
		}
	}
	
	public void setTown(String town) {
		if(person != null) {
			if(!this.town.equals(town)) {
				this.town = town;
				changeAddressRegion();
			}
		}else {
			this.town = town;
		}
	}
	
	public void setNeighborhood(String neighborhood) {
		if(person != null) {
			if(!this.neighborhood.equals(neighborhood)) {
				this.neighborhood = neighborhood;
				changeAddressRegion();
			}
		}else {
			this.neighborhood = neighborhood;
		}
	}
	
	public void setInactive(boolean inactive) {
		if(person != null) {
			if(this.inactive != inactive) {
				this.inactive = inactive;
				changeAddressAvailability(this.inactive);
			}
		}else {
			this.inactive = inactive;
		}
	}
	
	public void setCancelled(boolean cancelled) {
		if(person != null) {
			if(this.cancelled != cancelled) {
				this.cancelled = cancelled;
				changeAddressAvailability(this.cancelled);
			}
		}else {
			this.cancelled = cancelled;
		}
	}
	
	protected Address changeAddressRegion() {
		eventList.add(new AddressChangeRegionEvent(id, person.getId()));
		return this;
	}
	
	protected Address changeAddressAvailability(boolean isAvailable) {
		eventList.add(new AddressChangeAvailabilityEvent(id, person.getId(), isAvailable));
		return this;
	}
	
}
