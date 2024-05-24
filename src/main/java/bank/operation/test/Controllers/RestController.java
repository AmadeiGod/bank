package bank.operation.test.Controllers;


import bank.operation.test.DTO.LoginDto;
import bank.operation.test.Repository.PersonRepo;
import bank.operation.test.Service.PersonServ;
import bank.operation.test.Service.PersonServices;
import bank.operation.test.models.Person;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@Validated
@org.springframework.web.bind.annotation.RestController
public class RestController {

    public PersonServ personServ;
    @Autowired
    public PersonRepo personRepo;
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public PersonServices personServices;
    @PostMapping("/api/reg")
    public ResponseEntity<?> registerPerson(@RequestBody Person person){
        if (personRepo.findByEmail(person.getEmail()).isPresent()){
            return new ResponseEntity<>("Этот email уже используется", HttpStatus.OK);
        }
        if (personRepo.findByEmail(person.getPhoneNumber()).isPresent()){
            return new ResponseEntity<>("Этот номер уже используется", HttpStatus.OK);
        }
        personServices.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/update/email")
    public String updateEmail(Authentication authentication, @RequestBody Person person2 ){
        Person person = personRepo.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        if(personRepo.findByEmail(person2.getEmail()) != null){
            personRepo.updateEmail(person2.getEmail(),person.getId());
            return "Вы поменяли email";
        }
        return "Такой Email уже используется";
    }

    @PostMapping("/update/phone_number")
    public String updatePhone_number(Authentication authentication, @RequestBody Person person2 ){
        Person person = personRepo.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        if(personRepo.findByPhoneNumber(person2.getPhoneNumber()) != null){
            personRepo.updatePhoneNumber(person2.getPhoneNumber(), person.getId());
            return "Вы поменяли номер";
        }
        return "Такой номер уже используется";
    }
    @GetMapping("/delete/phone_number")
    public String deletePhone_number(Authentication authentication){
        Person person = personRepo.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        if(person.getEmail() != null){
            personRepo.deletePhoneNumber(person.getId());
        }else{
            return "Нельзя удалить номер";
        }
        return "Вы удалили номер";
    }
    @GetMapping("/delete/email")
    public String deleteEmail(Authentication authentication){
        Person person = personRepo.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        if(person.getPhoneNumber() != null){
            personRepo.deleteEmail(person.getId());
        }else{
            return "Нельзя удалить email";
        }
        return "Вы удалили email";
    }


    // поиск пользователей
    @GetMapping("/search")
    public List<Person> searchPerson(@RequestBody Person person){
        List<Person> list = new ArrayList<>();
        if(person.getPhoneNumber() != null){
            list = personRepo.findAllByPhoneNumber(person.getPhoneNumber());
            return list;
        }
        if(person.getEmail() != null){
            list = personRepo.findAllByEmail(person.getEmail());
            return list;
        }
        if(person.getFIO() != null){
            list = personRepo.findAllByFIO(person.getFIO());
            return list;
        }
        if(person.getBirthday() != null){
            list = personRepo.findAll();
            List<String> listInt = list.stream().map(Person::getBirthday).toList();
            List<Integer> listInt2 = listInt.stream().map(s -> s.replace(".","")).map(Integer::valueOf).toList();
            listInt2 = listInt2.stream().filter(s -> s > Integer.parseInt(person.getBirthday().replace(".",""))).collect(Collectors.toList());
            List<StringBuilder> str = new ArrayList<>();
            for ( int i = 0; i < listInt2.size(); i++){
                StringBuilder stringBuilder = new StringBuilder(listInt2.get(i).toString());
                stringBuilder.insert(4,".");
                stringBuilder.insert(7,".");
                str.add(stringBuilder);
            }
            List<Person> listPerson = str.stream().map(s -> personRepo.findByBirthday(String.valueOf(s))).collect(Collectors.toList());
            return listPerson;
        }

        return list;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        // Обработка исключения ConstraintViolationException
        return "Ваша сумма должна быть больше нуля";
    }
    @GetMapping("/send/{quantity}/{phoneNumber}")
    public synchronized String sendMoney(@PathVariable("phoneNumber") String phoneNumber,
                            @PathVariable("quantity") @Min(0) double quantity,
                            Authentication authentication) throws ConstraintViolationException {
        Person person = personRepo.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid"));
        if(person.getBank_count() > quantity){
            if(personRepo.findByPhoneNumber(phoneNumber).isPresent()){
                personRepo.updateBank_count(person.getBank_count() - quantity, person.getId());
                personRepo.updateBank_count(personRepo.findByPhoneNumber(phoneNumber).get().getBank_count() + quantity, personRepo.findByPhoneNumber(phoneNumber).get().getId());
                return quantity + " столько было переведено на счет " + phoneNumber;
            }
            return "НЕверный номер";
        }
        return "Недостаточно средств на счете";
    }
}
