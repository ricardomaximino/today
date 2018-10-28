package brasajava.person.message.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AddressBindingChannel {
	
	String ADDRESS_CREATED_EVENT = "send-address-created-event";
	String ADDRESS_UPDATED_EVENT = "send-address-updated-event";
	String ADDRESS_DELETED_EVENT = "send-address-deleted-event";

	@Output(ADDRESS_CREATED_EVENT)
	MessageChannel sendCreatedEvent();

	@Output(ADDRESS_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();
	
	@Output(ADDRESS_DELETED_EVENT)
	MessageChannel sendDeletedEvent();
}
