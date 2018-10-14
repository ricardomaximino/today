package brasajava.person.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AddressBindingChannel {
	
	String ADDRESS_CREATED_EVENT = "";
	String ADDRESS_UPDATED_EVENT = "";
	String ADDRESS_REGION_CHANGED_EVENT = "";
	String ADDRESS_ADDRESS_CHANGED_EVENT = "";

	@Output(ADDRESS_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	
	@Output(ADDRESS_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();

	@Output(ADDRESS_REGION_CHANGED_EVENT)
	MessageChannel sendRegionChangedEvent();
	
	@Output(ADDRESS_ADDRESS_CHANGED_EVENT)
	MessageChannel sendAddressChangedEvent();
}
