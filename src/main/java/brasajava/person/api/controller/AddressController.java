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

import brasajava.person.domain.entity.Address;
import brasajava.person.domain.entity.Person;
import brasajava.person.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person/{id}/address")
public class AddressController {

	private static final String USER = "ricardo";
	private PersonService service;
	
	public AddressController(PersonService service) {
		this.service = service;
	}
	
	@PostMapping
	public Mono<ResponseEntity<Person>> create(@PathVariable String id,@RequestBody Address address){
		return service.findById(id)
				.map(p -> p.addAddress(address, USER))
				.flatMap(p -> service.update(p, USER))
				.map(ResponseEntity::ok);
	}
	
	@PutMapping("/{addressId}")
	public Mono<ResponseEntity<Person>> update(@PathVariable String id, @PathVariable String addressId, @RequestBody Address address){
		return service.findById(id)
				.flatMap(p -> {
					if(p.getAddressById(addressId).isPresent()) {
						BeanUtils.copyProperties(address, p.getAddressById(addressId).get(), "id","auditInfo");
					}else {
						return Mono.empty();
					}
					return service.update(p, USER);
				})
				.map(ResponseEntity::ok);
	}
	
	@GetMapping
	public Flux<Address> findAll(@PathVariable String id){
		return service.findById(id)
				.flatMapMany(p -> Flux.fromIterable(p.getAddresses()));
	}
	
	@GetMapping("/{addressId}")
	public Mono<ResponseEntity<Address>> findById(@PathVariable String id, @PathVariable String addressId){
		return service.findById(id)
				.flatMap(p -> {
					if(p.getAddressById(addressId).isPresent()) {
						return Mono.just(p.getAddressById(addressId).get());
					}else {
						return Mono.empty();
					}
				})
				.map(ResponseEntity::ok);
	}
		
}
