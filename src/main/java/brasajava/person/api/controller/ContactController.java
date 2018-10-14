package brasajava.person.api.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brasajava.person.domain.entity.Contact;
import brasajava.person.domain.entity.Person;
import brasajava.person.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person/{id}/contact")
public class ContactController {

	private static final String USER = "ricardo";
	private PersonService service;
	
	public ContactController(PersonService service) {
		this.service = service;
	}
	
	@PostMapping
	public Mono<ResponseEntity<Person>> create(@PathVariable String id,@RequestBody Contact contact){
		return service.findById(id)
				.map(p -> p.addContact(contact, USER))
				.flatMap(p -> service.update(p, USER))
				.map(ResponseEntity::ok);
	}
	
	@PutMapping("/{contactId}")
	public Mono<ResponseEntity<Person>> update(@PathVariable String id, @PathVariable String contactId, @RequestBody Contact contact){
		return service.findById(id)
				.flatMap(p -> {
					if(p.getContactById(contactId).isPresent()) {
						BeanUtils.copyProperties(contact, p.getContactById(contactId).get(), "id","auditInfo");
					}else {
						return Mono.empty();
					}
					return service.update(p, USER);
				})
				.map(ResponseEntity::ok);
	}
	
	@GetMapping
	public Flux<Contact> findAll(@PathVariable String id){
		return service.findById(id)
				.flatMapMany(p -> Flux.fromIterable(p.getContacts()));
	}
	
	@GetMapping("/{contactId}")
	public Mono<ResponseEntity<Contact>> findById(@PathVariable String id, @PathVariable String contactId){
		return service.findById(id)
				.flatMap(p -> {
					if(p.getContactById(contactId).isPresent()) {
						return Mono.just(p.getContactById(contactId).get());
					}else {
						return Mono.empty();
					}
				})
				.map(ResponseEntity::ok);
	}
		
}
