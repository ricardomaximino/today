
package brasajava.person.message.event.service.impl;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import brasajava.person.message.event.PersonActivateEvent;
import brasajava.person.message.event.PersonCreateEvent;
import brasajava.person.message.event.PersonDeleteEvent;
import brasajava.person.message.event.PersonUpdateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonEventServiceImpl {
	
	
	public void sendPersonCreatedEvent(PersonCreateEvent event) {
		log.info("Person created event recieved by PERSON QUEUE SENDER SERVICE => " + MessageBuilder.withPayload(event).build());
	}
	
	public void sendPersonUpdatedEvent(PersonUpdateEvent event) {
		log.info("Person updated event recieved by PERSON QUEUE SENDER SERVICE => " + MessageBuilder.withPayload(event).build());
	}
	
	public void sendPersonActivatedEvent(PersonActivateEvent event) {
		log.info("Person activated event recieved by PERSON QUEUE SENDER SERVICE => " + MessageBuilder.withPayload(event).build());
	}
	
	public void sendPersonDeletedEvent(PersonDeleteEvent event) {
		log.info("Person deleted event recieved by PERSON QUEUE SENDER SERVICE => " + MessageBuilder.withPayload(event).build());
	}

}
