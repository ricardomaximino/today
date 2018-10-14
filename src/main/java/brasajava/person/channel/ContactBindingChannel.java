package brasajava.person.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ContactBindingChannel {
	
	String PERSON_CREATED_EVENT = "";
	String PERSON_UPDATED_EVENT = "";
	String PERSON_CONTACT_CHANGED_EVENT = "";
	String PERSON_AVAILABILITY_CHANGED_EVENT = "";

	@Output(PERSON_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	@Output(PERSON_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();

	@Output(PERSON_CONTACT_CHANGED_EVENT)
	MessageChannel sendContactChangedEvent();
	
	@Output(PERSON_AVAILABILITY_CHANGED_EVENT)
	MessageChannel sendAvailabilityChangedEvent();
}
