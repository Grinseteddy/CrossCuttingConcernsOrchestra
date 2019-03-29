package com.annegret.servicepattern.orchestra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class OrchestraApplication {

    static Logger log;

    static {
        log = Logger.getLogger(OrchestraApplication.class.getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(OrchestraApplication.class, args);
    }

}
