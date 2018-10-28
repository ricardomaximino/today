package brasajava.person.message.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ContactBindingChannel {
	
	String PERSON_CREATED_EVENT = "send-contact-created-event";
	String PERSON_CONTACT_CHANGED_EVENT = "send-contact-changed-event";
	String PERSON_AVAILABILITY_CHANGED_EVENT = "send-contact-availabilityChanged-event";
	String PERSON_DELETED_EVENT = "send-contact-deleted-event";

	@Output(PERSON_CREATED_EVENT)
	MessageChannel sendCreatedEvent();

	@Output(PERSON_CONTACT_CHANGED_EVENT)
	MessageChannel sendContactChangedEvent();
	
	@Output(PERSON_AVAILABILITY_CHANGED_EVENT)
	MessageChannel sendAvailabilityChangedEvent();

	@Output(PERSON_DELETED_EVENT)
	MessageChannel sendDeletedEvent();
}
