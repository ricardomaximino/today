package brasajava.person.domain.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import brasajava.person.domain.entity.Person;
import reactor.core.publisher.Flux;

public interface PersonRepository extends ReactiveMongoRepository<Person, String>{
	Flux<Person> findByBirthday(LocalDate date);
	Flux<Person> findByBirthdayBetween(LocalDate from, LocalDate to);
	Flux<Person> findByFirstname(String firstname);
	Flux<Person> findByLastname(String lastname);
}
