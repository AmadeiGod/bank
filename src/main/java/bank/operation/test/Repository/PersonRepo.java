package bank.operation.test.Repository;

import bank.operation.test.models.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByLoginOrEmail(String login, String email);
    Optional<Person> findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Optional<Person> findByPhoneNumber(String phone_number);
    List<Person> findAllByPhoneNumber(String phoneNumber);
    List<Person> findAllByEmail(String email);

    @Query(value = "select * from person where fio LIKE %:fio%", nativeQuery = true)
    List<Person> findAllByFIO(@Param("fio") String fio);
    @Transactional
    @Modifying
    @Query(value = "update Person set email = %:email% where id = %:id%", nativeQuery = true)
    void updateEmail(@Param("email") String email, @Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "update Person set phone_number = %:phoneNumber% where id = %:id%", nativeQuery = true)
    void updatePhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "update Person set phone_number = null where id = %:id%", nativeQuery = true)
    void deletePhoneNumber(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "update Person set email = null where id = %:id%", nativeQuery = true)
    void deleteEmail(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "update Person set bank_count = %:bank_count% where id = %:id%", nativeQuery = true)
    void updateBank_count(@Param("bank_count") double bank_count,@Param("id") Long id);
    List<Person> findAllByBirthday(String birthday);
    Person findByBirthday(String birthday);
}
