package gov.bacen.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PixBacenSimulatorApplication {

    static void main(String[] args) {
        SpringApplication.run(PixBacenSimulatorApplication.class, args);
    }
}
