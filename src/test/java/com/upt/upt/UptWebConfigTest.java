package com.upt.upt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UptWebConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testCorsConfigurerBeanExists() {
        WebMvcConfigurer corsConfigurer = applicationContext.getBean(WebMvcConfigurer.class);
        assertNotNull(corsConfigurer, "The CORS configurer bean should not be null");
    }
}