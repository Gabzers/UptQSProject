package com.upt.upt;

import com.upt.upt.service.UserService;
import com.upt.upt.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class UptWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UptWebController uptWebController;

    private MockHttpSession session;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and prepares the session.
     * 
     */
    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
    }

    /**
     * Tests the login page endpoint.
     * 
     * @throws Exception if an error occurs during the request
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    /**
     * Tests the login validation endpoint for a successful login.
     * 
     * @throws Exception if an error occurs during the request
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    @Test
    public void testValidateLoginSuccess() throws Exception {
        when(userService.validateUser("user", "password")).thenReturn(UserType.MASTER);
        when(userService.getUserIdByUsername("user", UserType.MASTER)).thenReturn(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/validate-login")
                        .param("username", "user")
                        .param("password", "password")
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/master"))
                .andReturn();

        verify(session, times(1)).setAttribute("userId", 1L);
        verify(session, times(1)).setAttribute("userType", UserType.MASTER);
        verify(session, times(1)).setAttribute("username", "user");
    }

    /**
     * Tests the login validation endpoint for a failed login.
     * 
     * @throws Exception if an error occurs during the request
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    @Test
    public void testValidateLoginFailure() throws Exception {
        when(userService.validateUser(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/validate-login")
                        .param("username", "user")
                        .param("password", "wrongpassword")
                        .session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error=Invalid credentials"));
    }

    /**
     * Tests the logout endpoint.
     * 
     * @throws Exception if an error occurs during the request
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout").session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        verify(session, times(1)).invalidate();
    }
}
