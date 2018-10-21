package brasajava.person.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PersonBindingChannel {
	
	String PERSON_CREATED_EVENT = "send-person-created-event";
	String PERSON_UPDATED_EVENT = "send-person-updated-event";
	String PERSON_ACTIVATED_EVENT = "send-person-actived-event";
	String PERSON_DELETED_EVENT = "send-person-deleted-event";

	@Output(PERSON_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	@Output(PERSON_ACTIVATED_EVENT)
	MessageChannel sendActivatedEvent();
	
	@Output(PERSON_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();
	
	@Output(PERSON_DELETED_EVENT)
	MessageChannel sendDeletedEvent();

}
