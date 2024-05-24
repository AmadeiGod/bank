package bank.operation.test.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;

    private double bank_count;
    @Column(name = "depozit")
    private double depozit;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String FIO; // ФИО
    private String role;
}

