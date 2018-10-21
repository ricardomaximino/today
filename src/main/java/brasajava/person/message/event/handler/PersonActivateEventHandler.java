package brasajava.person.message.event.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import brasajava.person.message.event.PersonActivateEvent;
import brasajava.person.message.event.service.impl.PersonEventServiceImpl;

@Component
public class PersonActivateEventHandler {

	private PersonEventServiceImpl service;
	public PersonActivateEventHandler(PersonEventServiceImpl service) {
		this.service = service;
	}
	
	@EventListener
	public void handle(PersonActivateEvent event) {
		service.sendPersonActivatedEvent(event);
	}
}
