package com.example.demo;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    private static Log logger = LogFactory.getLog(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        logger.error("333");
        logger.info("信息");

    }

}
