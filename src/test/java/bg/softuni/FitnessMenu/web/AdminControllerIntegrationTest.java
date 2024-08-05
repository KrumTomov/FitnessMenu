package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.view.UserViewModel;
import bg.softuni.FitnessMenu.repository.CommentRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.RecipeService;
import bg.softuni.FitnessMenu.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void testGetAllUsers() throws Exception {
        List<UserViewModel> mockUsers = Collections.singletonList(new UserViewModel());
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/all-users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin-users-all"))
                .andExpect(model().attribute("users", mockUsers));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.findAllByAuthor_Id(userId)).thenReturn(Collections.emptyList());


        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/delete-user/{id}", userId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/all-users"));


        verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void testMarkUserAsActive() throws Exception {
        Long userId = 1L;
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername(username);
        userEntity.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/activate-user/{id}", userId)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/inactive-users"));


        verify(userService).activateUserById(userId);

    }
}
