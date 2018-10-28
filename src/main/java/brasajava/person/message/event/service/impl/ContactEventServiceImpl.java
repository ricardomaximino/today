package brasajava.person.message.event.service.impl;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import brasajava.person.message.channel.ContactBindingChannel;
import brasajava.person.message.event.ContactAvailabilityChangeEvent;
import brasajava.person.message.event.ContactChangeEvent;
import brasajava.person.message.event.ContactCreateEvent;
import brasajava.person.message.event.ContactDeleteEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(ContactBindingChannel.class)
public class ContactEventServiceImpl {

	private ContactBindingChannel channels;

	public ContactEventServiceImpl(ContactBindingChannel channels) {
		this.channels = channels;
	}
	
	public void sendCreatedEvent(ContactCreateEvent event) {
		channels.sendCreatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Contact created event recieved by CONTACT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendChangedEvent(ContactChangeEvent event) {
		channels.sendContactChangedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Contact changed event recieved by CONTACT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendAvailabilityChangedEvent(ContactAvailabilityChangeEvent event) {
		channels.sendAvailabilityChangedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Contact availability changed event recieved by CONTACT QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendDeletedEvent(ContactDeleteEvent event) {
		channels.sendDeletedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Contact deleted event recieved by CONTACT QUEUE SENDER SERVICE => {}",event);
	}
}
