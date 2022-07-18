package project.myinstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyinstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyinstagramApplication.class, args);
    }

}
