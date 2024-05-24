package bank.operation.test.Service;

import bank.operation.test.Repository.PersonRepo;
import bank.operation.test.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
public class PersonDetailsImpl  implements PersonServices {
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public PersonDetailsImpl() {

    }
    public PersonDetailsImpl(PersonRepo personRepo, BCryptPasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person person = personRepo.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ login));
        Set<GrantedAuthority> authorities = null;
        return new PersonServ(person);
    }

    @Override
    public Person findByUsername(String username) {
        return null;
    }

    @Override
    public Person findUserByUsername(String username) {
        return null;
    }

    @Override
    public List<Person> getAll() {
        return null;
    }

    @Override
    public Person save(Person person1) {

        Person person = new Person();
        person.setLogin(person1.getLogin());
        person.setEmail(person1.getEmail());
        person.setPassword(passwordEncoder.encode(person1.getPassword()));
        person.setRole("USER");
        person.setPhoneNumber(person1.getPhoneNumber());
        person.setBank_count(person1.getBank_count());

        return personRepo.save(person);
    }

}
