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
import org.springframework.mock.web.MockHttpSession;  // Use MockHttpSession aqui
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import jakarta.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class UptWebControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc é automaticamente injetado

    @MockBean
    private UserService userService; // Mock do UserService para simular o comportamento do serviço

    @InjectMocks
    private UptWebController uptWebController; // Injetando o controlador a ser testado

    private MockHttpSession session; // Usando MockHttpSession

    @BeforeEach
    public void setUp() {
        // Inicializando mocks e preparando o ambiente antes de cada teste
        session = new MockHttpSession(); // Usando MockHttpSession para simular a sessão
    }

    @Test
    public void testLoginPage() throws Exception {
        // Testando o endpoint de login
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login")); // Verifica se a view correta é retornada
    }

    @Test
    public void testValidateLoginSuccess() throws Exception {
        // Definindo o comportamento do mock do UserService
        when(userService.validateUser("user", "password")).thenReturn(UserType.MASTER);
        when(userService.getUserIdByUsername("user", UserType.MASTER)).thenReturn(1L);

        // Testando o endpoint de validação de login
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/validate-login")
                        .param("username", "user")
                        .param("password", "password")
                        .session(session)) // Usando MockHttpSession aqui
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Espera redirecionamento
                .andExpect(MockMvcResultMatchers.redirectedUrl("/master")) // Verifica o redirecionamento correto
                .andReturn();

        // Verifica se os atributos da sessão foram setados corretamente
        verify(session, times(1)).setAttribute("userId", 1L);
        verify(session, times(1)).setAttribute("userType", UserType.MASTER);
        verify(session, times(1)).setAttribute("username", "user");
    }

    @Test
    public void testValidateLoginFailure() throws Exception {
        // Definindo o comportamento do mock do UserService para falha
        when(userService.validateUser(anyString(), anyString())).thenReturn(null);

        // Testando o endpoint de validação de login com credenciais erradas
        mockMvc.perform(MockMvcRequestBuilders.post("/validate-login")
                        .param("username", "user")
                        .param("password", "wrongpassword")
                        .session(session)) // Usando MockHttpSession aqui
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // Espera redirecionamento
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error=Invalid credentials")); // Verifica o redirecionamento correto para erro
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout").session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        verify(session, times(1)).invalidate();
    }
}
