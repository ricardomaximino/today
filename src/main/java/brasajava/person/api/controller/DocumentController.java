package brasajava.person.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import brasajava.person.domain.entity.Person;
import brasajava.person.domain.entity.PersonalDocument;
import brasajava.person.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person/{id}/document")
public class DocumentController {

	private static final String USER = "ricardo";
	private PersonService service;
	
	public DocumentController(PersonService service) {
		this.service = service;
	}
	
	@PostMapping
	public Mono<ResponseEntity<Person>> create(@PathVariable String id,@RequestBody PersonalDocument document){
		return service.findById(id)
				.map(p -> p.addDocument(document, USER))
				.flatMap(p -> service.update(p, USER))
				.map(ResponseEntity::ok);
	}
	
	@PutMapping("/{documentId}")
	public Mono<ResponseEntity<Person>> update(@PathVariable String id, @PathVariable String documentId, @RequestBody PersonalDocument document){
		return service.findById(id)
					.flatMap(p -> service.update(p.updateDocument(documentId, document, USER), USER))
					.map(ResponseEntity::ok);
	}
	
	@GetMapping
	public Flux<PersonalDocument> findAll(@PathVariable String id){
		return service.findById(id)
				.flatMapMany(p -> Flux.fromIterable(p.getDocuments()));
	}
	
	@GetMapping("/{documentId}")
	public Mono<ResponseEntity<PersonalDocument>> findById(@PathVariable String id, @PathVariable String documentId){
		return service.findById(id)
				.flatMap(p -> {
					if(p.getDocumentById(documentId).isPresent()) {
						return Mono.just(p.getDocumentById(documentId).get());
					}else {
						return Mono.empty();
					}
				})
				.map(ResponseEntity::ok);
	}
		
}
