package bank.operation.test.Service;

import bank.operation.test.models.Person;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PersonServices extends UserDetailsService {
    Person findByUsername(String username);

    Person findUserByUsername(String username);

    List<Person> getAll();

    Person save(Person Person);

}
