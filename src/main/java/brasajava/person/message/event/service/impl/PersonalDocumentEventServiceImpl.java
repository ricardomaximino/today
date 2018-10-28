package brasajava.person.message.event.service.impl;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import brasajava.person.message.channel.PersonalDocumentBindingChannel;
import brasajava.person.message.event.PersonalDocumentCancelEvent;
import brasajava.person.message.event.PersonalDocumentCreateEvent;
import brasajava.person.message.event.PersonalDocumentExpiredEvent;
import brasajava.person.message.event.PersonalDocumentUpdateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(PersonalDocumentBindingChannel.class)
public class PersonalDocumentEventServiceImpl {

	private PersonalDocumentBindingChannel channels;

	public PersonalDocumentEventServiceImpl(PersonalDocumentBindingChannel channels) {
		this.channels = channels;
	}
	
	public void sendCreatedEvent(PersonalDocumentCreateEvent event) {
		channels.sendCreatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Personal Document created event recieved by PERSONAL DOCUMENT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendUpdatedEvent(PersonalDocumentUpdateEvent event) {
		channels.sendUpdatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Personal Document updated event recieved by PERSONAL DOCUMENT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendExpiredEvent(PersonalDocumentExpiredEvent event) {
		channels.sendExpiredEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Personal Document expiredEvent event recieved by PERSONAL DOCUMENT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendCancelledEvent(PersonalDocumentCancelEvent event) {
		channels.sendCancelledEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Personal Document cancelled event recieved by PERSONAL DOCUMENT QUEUE SENDER SERVICE => {}",event);
	}
}
