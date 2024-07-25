package bg.softuni.FitnessMenu.service;



import bg.softuni.FitnessMenu.model.service.RecipeAddServiceModel;
import bg.softuni.FitnessMenu.model.view.AllRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.RecipeDetailsViewModel;

import java.util.List;

public interface RecipeService extends CommonService {
    void createRecipe(RecipeAddServiceModel recipeAddServiceModel, String username);

    List<AllRecipesViewModel> getAllRecipes();

    RecipeDetailsViewModel findRecipeById(Long id);

    List<RecipeDetailsViewModel> getAllRecipesDetails();
}
