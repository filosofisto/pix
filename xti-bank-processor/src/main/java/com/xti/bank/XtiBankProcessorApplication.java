package com.xti.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XtiBankProcessorApplication {

    static void main(String[] args) {
        SpringApplication.run(XtiBankProcessorApplication.class, args);
    }
}
