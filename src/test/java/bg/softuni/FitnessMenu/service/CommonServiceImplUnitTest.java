package bg.softuni.FitnessMenu.service;

import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.repository.CommentRepository;
import bg.softuni.FitnessMenu.repository.RecipeRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.impl.CommonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommonServiceImplUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private CommonServiceImpl commonService;

    private UserEntity user;
    private RecipeEntity recipe;

    @BeforeEach
    public void setUp() {

        user = new UserEntity();
        user.setUsername("testUser");

        recipe = new RecipeEntity();
        recipe.setId(1L);
        recipe.setTitle("Test Recipe");
        recipe.setAuthor(user);
        user.getCreatedRecipes().add(recipe);
        user.getFavoriteRecipes().add(recipe);
    }

    @Test
    public void testDeleteRecipeById() {
        // Arrange
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        commonService.deleteRecipeById(1L, "testUser");

        // Assert
        assertFalse(user.getCreatedRecipes().contains(recipe));
    }

    @Test
    public void testRemoveFromAllFavorites() {
        UserEntity user2 = new UserEntity();
        user2.setFavoriteRecipes(new HashSet<>(List.of(recipe)));
        // Arrange
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        commonService.removeFromAllFavorites(1L);

        // Assert
        assertFalse(user.getFavoriteRecipes().contains(recipe));
        assertFalse(user2.getFavoriteRecipes().contains(recipe));
    }
}