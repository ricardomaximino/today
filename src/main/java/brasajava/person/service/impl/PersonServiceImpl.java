package brasajava.person.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import brasajava.person.domain.entity.Person;
import brasajava.person.domain.repository.PersonRepository;
import brasajava.person.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonServiceImpl implements PersonService{

	private PersonRepository repository;
	
	PersonServiceImpl(PersonRepository repository){
		this.repository = repository;
	}

	@Override
	public Mono<Person> create(Person person, String user) {
		return repository.save(person.onCreate(user));
	}

	@Override
	public Mono<Person> update(String id, Person person, String user) {
		return repository.findById(id).map(p -> p).flatMap(p -> repository.save(p.onUpdate(user)));
	}
	
	@Override
	public Mono<Person> update(Person person, String user) {
		return repository.save(person.onUpdate(user));
	}

	@Override
	public Mono<Void> delete(String id, String user) {
		return repository.deleteById(id);
	}

	@Override
	public Mono<Person> findById(String id) {
		return repository.findById(id);
	}

	@Override
	public Flux<Person> findAll(int numberOfRecordsPerChunk) {
		return repository.findAll();
	}

	@Override
	public Flux<Person> findByBirthday(LocalDate date) {
		return repository.findByBirthday(date);
	}

	@Override
	public Flux<Person> findByBirthdayBetween(LocalDate from, LocalDate to) {
		return repository.findByBirthdayBetween(from, to);
	}

	@Override
	public Flux<Person> findByFirstname(String firstname) {
		return repository.findByFirstname(firstname);
	}

	@Override
	public Flux<Person> findByLastname(String lastname) {
		return repository.findByLastname(lastname);
	}
}
