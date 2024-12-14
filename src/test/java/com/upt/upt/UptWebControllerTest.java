package com.upt.upt;

import com.upt.upt.UptWebController;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UptWebControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private DirectorUnitService directorUnitService;

    @Test
    public void testHomePage() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/", String.class);
        assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/login");
    }

    @Test
    public void testLoginPage() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/login", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("login");
    }

    @Test
    public void testLogout() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/logout", String.class);
        assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/login");
    }

    @Test
    public void testValidateLoginSuccess() {
        // Setup userService to return a valid user type and user ID
        // This part assumes you have a way to set up the userService for testing purposes

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/validate-login?username=testuser&password=testpass", null, String.class);
        assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/master");
    }

    @Test
    public void testValidateLoginFailure() {
        // Setup userService to return null for invalid credentials
        // This part assumes you have a way to set up the userService for testing purposes

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/validate-login?username=invaliduser&password=invalidpass", null, String.class);
        assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/login?error=Invalid credentials");
    }
}