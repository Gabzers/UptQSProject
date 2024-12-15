package com.upt.upt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UptWebApplicationTests {

    @Test
    void contextLoads() {
        UptWebApplication application = new UptWebApplication();
        assertThat(application).isNotNull();
    }

}