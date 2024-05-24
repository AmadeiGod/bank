package bank.operation.test.Service;

import bank.operation.test.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;


public class PersonServ implements UserDetails {

    private final Person person;

    public PersonServ(Person person) {
        this.person = person;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(person.getRole());
        return Arrays.asList(authority);
    }

    private String getEmail(){
        return person.getEmail();
    }
    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
