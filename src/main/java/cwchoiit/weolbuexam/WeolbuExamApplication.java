package cwchoiit.weolbuexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeolbuExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeolbuExamApplication.class, args);
    }
}
