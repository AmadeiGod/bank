package bank.operation.test.Config;

import bank.operation.test.Repository.PersonRepo;
import bank.operation.test.Service.PersonDetailsImpl;
import bank.operation.test.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

import java.util.List;

@Configuration
@EnableScheduling
public class ConfigScheduling {
    @Autowired
    public PersonDetailsImpl personDetails;
    @Autowired
    public PersonRepo personRepo;
    @Scheduled(fixedDelay = 1000)
    public void bankmoney() throws InterruptedException {
        Thread.sleep(3000);
        List<Person> list = personRepo.findAll();
        for (int i = 0; i < personRepo.findAll().size(); i++){
            Person person = list.get(i);
            if(person.getBank_count() < person.getDepozit()*2.07 ){
                personRepo.updateBank_count(person.getBank_count() + person.getBank_count()*0.05,person.getId());
            }

        }
    }
}
