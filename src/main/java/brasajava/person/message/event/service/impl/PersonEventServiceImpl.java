
package brasajava.person.message.event.service.impl;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import brasajava.person.message.channel.PersonBindingChannel;
import brasajava.person.message.event.PersonActivateEvent;
import brasajava.person.message.event.PersonCreateEvent;
import brasajava.person.message.event.PersonDeleteEvent;
import brasajava.person.message.event.PersonUpdateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(PersonBindingChannel.class)
public class PersonEventServiceImpl {
	
	private PersonBindingChannel channels;

	public PersonEventServiceImpl(PersonBindingChannel channels) {
		this.channels = channels;
	}
	
	public void sendPersonCreatedEvent(PersonCreateEvent event) {
		channels.sendCreatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Person created event recieved by PERSON QUEUE SENDER SERVICE => {}",event);
		
	}
	
	public void sendPersonUpdatedEvent(PersonUpdateEvent event) {
		channels.sendUpdatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Person updated event recieved by PERSON QUEUE SENDER SERVICE => {}", event);
	}
	
	public void sendPersonActivatedEvent(PersonActivateEvent event) {
		channels.sendActivatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Person activated event recieved by PERSON QUEUE SENDER SERVICE => {}", event);
	}
	
	public void sendPersonDeletedEvent(PersonDeleteEvent event) {
		channels.sendDeletedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Person deleted event recieved by PERSON QUEUE SENDER SERVICE => {}", event);
	}

}
