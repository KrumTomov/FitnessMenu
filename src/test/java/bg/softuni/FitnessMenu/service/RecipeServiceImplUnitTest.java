package bg.softuni.FitnessMenu.service;


import bg.softuni.FitnessMenu.exception.ObjectNotFoundException;
import bg.softuni.FitnessMenu.model.entity.CategoryEntity;
import bg.softuni.FitnessMenu.model.entity.IngredientEntity;
import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.entity.enums.CategoryName;
import bg.softuni.FitnessMenu.model.service.RecipeAddServiceModel;
import bg.softuni.FitnessMenu.model.view.AllRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.RecipeDetailsViewModel;
import bg.softuni.FitnessMenu.repository.CategoryRepository;
import bg.softuni.FitnessMenu.repository.IngredientRepository;
import bg.softuni.FitnessMenu.repository.RecipeRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    public void testCreateRecipe() {
        // Arrange
        RecipeAddServiceModel recipeAddServiceModel = new RecipeAddServiceModel();
        recipeAddServiceModel.setIngredientNames("Ingredient1, Ingredient2");
        recipeAddServiceModel.setCategory(CategoryName.BEVERAGES);

        UserEntity user = new UserEntity();
        user.setUsername("testUser");

        CategoryEntity category = new CategoryEntity();
        category.setName(CategoryName.BEVERAGES);

        RecipeEntity recipe = new RecipeEntity();
        recipe.setAuthor(user);
        recipe.setCategory(category);

        List<IngredientEntity> ingredients = new ArrayList<>();
        ingredients.add(new IngredientEntity("Ingredient1"));
        ingredients.add(new IngredientEntity("Ingredient2"));

        recipe.setIngredients(ingredients);

        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(categoryRepository.getByNameIs(CategoryName.BEVERAGES)).thenReturn(category);

        when(ingredientRepository.findByName("Ingredient1")).thenReturn(Optional.of(new IngredientEntity("Ingredient1")));
        when(ingredientRepository.findByName("Ingredient2")).thenReturn(Optional.of(new IngredientEntity("Ingredient2")));

        when(modelMapper.map(recipeAddServiceModel, RecipeEntity.class)).thenReturn(recipe);

        // Act
        recipeService.createRecipe(recipeAddServiceModel, "testUser");

        assertEquals(1, user.getCreatedRecipes().size());
    }
    @Test
    public void testGetAllRecipes() {
        List<RecipeEntity> recipes = new ArrayList<>();
        recipes.add(new RecipeEntity());
        recipes.add(new RecipeEntity());

        when(recipeRepository.findAll()).thenReturn(recipes);

        List<AllRecipesViewModel> allRecipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), allRecipes.size());
    }

    @Test
    public void testFindRecipeById_RecipeExists() {
        // Arrange
        Long recipeId = 1L;

        RecipeEntity mockRecipeEntity = new RecipeEntity();
        mockRecipeEntity.setId(recipeId);

        RecipeDetailsViewModel recipeDetailsViewModel = new RecipeDetailsViewModel();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipeEntity));
        when(modelMapper.map(mockRecipeEntity, RecipeDetailsViewModel.class)).thenReturn(recipeDetailsViewModel);

        // Act
        RecipeDetailsViewModel result = recipeService.findRecipeById(recipeId);

        // Assert
        assertEquals(recipeDetailsViewModel, result);

    }

    @Test
    public void testFindRecipeById_RecipeNotExists() {
        Long nonExistingRecipeId = 999L;
        when(recipeRepository.findById(nonExistingRecipeId)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> recipeService.findRecipeById(nonExistingRecipeId));

    }

}