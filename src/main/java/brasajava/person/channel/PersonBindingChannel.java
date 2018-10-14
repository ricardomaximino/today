package brasajava.person.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PersonBindingChannel {
	
	String PERSON_CREATED_EVENT = "";
	String PERSON_UPDATED_EVENT = "";
	String PERSON_ACTIVATED_EVENT = "";

	@Output(PERSON_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	@Output(PERSON_ACTIVATED_EVENT)
	MessageChannel sendActivatedEvent();
	
	@Output(PERSON_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();

}
