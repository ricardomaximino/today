package brasajava.person.message.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PersonalDocumentBindingChannel {
	
	String PERSONAL_DOCUMENT_CREATED_EVENT = "send-personalDocument-created-event";
	String PERSONAL_DOCUMENT_UPDATED_EVENT = "send-personalDocument-updated-event";
	String PERSONAL_DOCUMENT_EXPIRED_EVENT = "send-personalDocument-expired-event";
	String PERSONAL_DOCUMENT_CANCELLED_EVENT = "send-personalDocument-cancelled-event";

	@Output(PERSONAL_DOCUMENT_CREATED_EVENT)
	MessageChannel sendCreatedEvent();
	
	@Output(PERSONAL_DOCUMENT_UPDATED_EVENT)
	MessageChannel sendUpdatedEvent();

	@Output(PERSONAL_DOCUMENT_EXPIRED_EVENT)
	MessageChannel sendExpiredEvent();
	
	@Output(PERSONAL_DOCUMENT_CANCELLED_EVENT)
	MessageChannel sendCancelledEvent();
}
