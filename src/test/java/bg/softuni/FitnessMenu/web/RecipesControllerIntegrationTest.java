package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.model.binding.RecipeAddBindingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class RecipesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private RecipesController recipesController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipesController).build();
    }

    @Test
    public void testAddRecipe() throws Exception {
        mockMvc.perform(get("/recipes/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe-add"));
    }

    @Test
    public void testAddRecipePostWithErrors() throws Exception {
        RecipeAddBindingModel invalidRecipe = new RecipeAddBindingModel();
        mockMvc.perform(post("/recipes/add")
                        .flashAttr("org.springframework.validation.BindingResult.recipeAddBindingModel", invalidRecipe)
                        .flashAttr("recipeAddBindingModel", invalidRecipe))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:add"));
    }

}
