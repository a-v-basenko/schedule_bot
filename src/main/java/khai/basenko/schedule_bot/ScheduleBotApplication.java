package khai.basenko.schedule_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ScheduleBotApplication {

    public static void main(String[] args) {
        log.info("\n" + "-".repeat(36) + "START" + "-".repeat(36));
        SpringApplication.run(ScheduleBotApplication.class, args);
    }

}
