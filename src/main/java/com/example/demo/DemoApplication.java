package com.example.demo;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    private static Logger logger = Logger.getLogger("ingo");

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        logger.info("2222");
        try {
            Integer a = 1 / 0;
        } catch (Exception e) {

            logger.error(e.getMessage());

        }


    }


}
