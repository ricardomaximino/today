package brasajava.person.message.event.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import brasajava.person.message.event.PersonDeleteEvent;
import brasajava.person.message.event.service.impl.PersonEventServiceImpl;

@Component
public class PersonDeleteEventHandler {

	private PersonEventServiceImpl service;
	public PersonDeleteEventHandler(PersonEventServiceImpl service) {
		this.service = service;
	}
	
	@EventListener
	public void handle(PersonDeleteEvent event) {
		service.sendPersonDeletedEvent(event);
	}
}
