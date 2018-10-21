
package brasajava.person.service;

import java.time.Duration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.messaging.support.MessageBuilder;

import brasajava.person.channel.PersonBindingChannel;
import brasajava.person.domain.event.PersonCreateEvent;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

//@EnableAutoConfiguration
//@EnableBinding(PersonBindingChannel.class)
public class PersonEventService {
	
	private DirectProcessor<Object> created = DirectProcessor.create();
	
	@StreamEmitter
	@Output(PersonBindingChannel.PERSON_CREATED_EVENT)
	  public void emit(FluxSender output) {
		output.send(Flux.interval(Duration.ofSeconds(3)).map(t -> "Hello World"));
	  }
	
	public void sendPersonCreatedEvent(PersonCreateEvent event) {
		created.onNext(MessageBuilder.withPayload(event).build());	
	}

}
