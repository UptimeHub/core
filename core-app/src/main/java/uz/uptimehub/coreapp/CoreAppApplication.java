package uz.uptimehub.coreapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CoreAppApplication {

     static void main(String[] args) {
        SpringApplication.run(CoreAppApplication.class, args);
    }

}
