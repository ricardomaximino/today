package brasajava.person.message.event.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import brasajava.person.message.event.PersonCreateEvent;
import brasajava.person.message.event.service.impl.PersonEventServiceImpl;

@Component
public class PersonCreateEventHandler {

	private PersonEventServiceImpl service;
	public PersonCreateEventHandler(PersonEventServiceImpl service) {
		this.service = service;
	}
	
	@EventListener
	public void handle(PersonCreateEvent event) {
		service.sendPersonCreatedEvent(event);
	}
}
