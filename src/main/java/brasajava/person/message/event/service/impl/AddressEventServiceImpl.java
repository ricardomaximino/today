package brasajava.person.message.event.service.impl;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import brasajava.person.message.channel.AddressBindingChannel;
import brasajava.person.message.event.AddressCreateEvent;
import brasajava.person.message.event.AddressDeleteEvent;
import brasajava.person.message.event.AddressUpdateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(AddressBindingChannel.class)
public class AddressEventServiceImpl {

	private AddressBindingChannel channels;

	public AddressEventServiceImpl(AddressBindingChannel channels) {
		this.channels = channels;
	}

	public void sendCreatedEvent(AddressCreateEvent event) {
		channels.sendCreatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Address created event recieved by ADDRESS QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendUpdatedEvent(AddressUpdateEvent event) {
		channels.sendUpdatedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Address updated event recieved by ADDRESS QUEUE SENDER SERVICE => {}",event);
	}
	
	public void sendDeletedEvent(AddressDeleteEvent event) {
		channels.sendDeletedEvent().send(MessageBuilder.withPayload(event).build());
		log.info("Address deleted event recieved by ADDRESS QUEUE SENDER SERVICE => {}",event);;
	}
}
