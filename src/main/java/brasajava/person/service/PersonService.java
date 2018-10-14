package brasajava.person.service;

import java.time.LocalDate;

import brasajava.person.domain.entity.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {

	Mono<Person> create(Person person, String user);
	Mono<Person> update(String id,Person person, String user);
	Mono<Person> update(Person person, String user);
	Mono<Void> delete(String id, String user);
	Mono<Person> findById(String id);
	Flux<Person> findAll(int numberOfRecordsPerChunk);
	Flux<Person> findByBirthday(LocalDate date);
	Flux<Person> findByBirthdayBetween(LocalDate from, LocalDate to);
	Flux<Person> findByFirstname(String firstname);
	Flux<Person> findByLastname(String lastname);
}
