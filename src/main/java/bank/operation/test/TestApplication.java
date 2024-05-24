package bank.operation.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Timer;
import java.util.TimerTask;


@SpringBootApplication
public class TestApplication {
	static final Logger log =
			LoggerFactory.getLogger(TestApplication.class);
	public static void main(String[] args) {SpringApplication.run(TestApplication.class, args);}

}
