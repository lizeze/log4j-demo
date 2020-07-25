package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    private static Log logger = LogFactory.getLog(DemoApplicationTests.class);

    @Test
    void contextLoads() {

        logger.error("333");
        logger.info("333");
        logger.debug("333");
    }

}
