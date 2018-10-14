package brasajava.person.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PersonalDocumentBindingChannel {
	
	String PERSON_CREATED_EVENT = "";
	String PERSON_UPDATED_EVENT = "";
	String PERSON_EXPIRED_EVENT = "";
	String PERSON_CANCELLED_EVENT = "";

	@Output(PERSON_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	@Output(PERSON_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();

	@Output(PERSON_EXPIRED_EVENT)
	MessageChannel sendExpiredEvent();
	
	@Output(PERSON_CANCELLED_EVENT)
	MessageChannel sendCancelledEvent();
}
