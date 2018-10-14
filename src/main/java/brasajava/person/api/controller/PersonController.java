package brasajava.person.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brasajava.person.domain.entity.Person;
import brasajava.person.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	private static final String USER = "ricardo";
	private PersonService service;

	public PersonController(PersonService service) {
		this.service = service;
	}

	@PostMapping
	public Mono<ResponseEntity<Person>> create(@RequestBody Person person){
		return service.create(person, USER).map(ResponseEntity::ok);
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Person>> update(@PathVariable String id,@RequestBody Person person){
		return service.update(id, person, USER).map(ResponseEntity::ok);
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> deleteById(@PathVariable String id){
		return service.delete(id, USER);
	}
	
	@GetMapping
	public Flux<Person> findAll(){
		return service.findAll(0);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Person>> getById(@PathVariable String id){
		return service.findById(id).map(ResponseEntity::ok);
	}
}
