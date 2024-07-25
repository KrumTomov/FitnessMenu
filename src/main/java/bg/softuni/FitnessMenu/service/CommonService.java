package bg.softuni.FitnessMenu.service;

public interface CommonService {

    void deleteRecipeById(Long id, String username);

    void removeFromAllFavorites(Long recipeId);
}
