package com.upt.upt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UptWebApplicationTest {

    /**
     * Tests if the application context loads successfully.
     * 
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    @Test
    void contextLoads() {
        UptWebApplication application = new UptWebApplication();
        assertThat(application).isNotNull();
    }
}