package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;


    @Test
    public void testRegisterPostSuccess() throws Exception {

        mockMvc.perform(post("/users/register").
                        param("email", "test@example.com").
                        param("firstName", "Test").
                        param("lastName", "Testov").
                        param("password", "testPassword").
                        param("confirmPassword", "testPassword").
                        param("age", "10").
                        with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors());
    }

}
