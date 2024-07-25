package bg.softuni.FitnessMenu.service.impl;

import bg.softuni.FitnessMenu.exception.ObjectNotFoundException;
import bg.softuni.FitnessMenu.model.entity.CategoryEntity;
import bg.softuni.FitnessMenu.model.entity.IngredientEntity;
import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.service.RecipeAddServiceModel;
import bg.softuni.FitnessMenu.model.view.AllRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.RecipeDetailsViewModel;
import bg.softuni.FitnessMenu.repository.*;
import bg.softuni.FitnessMenu.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl extends CommonServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecipeServiceImpl(
            UserRepository userRepository,
            RecipeRepository recipeRepository,
            CommentRepository commentRepository,
            CategoryRepository categoryRepository,
            IngredientRepository ingredientRepository,
            ModelMapper modelMapper) {
        super(userRepository, recipeRepository, commentRepository);
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createRecipe(RecipeAddServiceModel recipeAddServiceModel, String username) {
        String[] ingredientNames = recipeAddServiceModel.getIngredientNames().split(",\\s*");
        List<IngredientEntity> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientNames.length; i++) {
            String ingredientName = ingredientNames[i];
            IngredientEntity ingredient;
            Optional<IngredientEntity> optionalIngredient = ingredientRepository.findByName(ingredientName);

            if (optionalIngredient.isEmpty()) {
                ingredient = new IngredientEntity(ingredientName);
                ingredientRepository.saveAndFlush(ingredient);
            } else {
                ingredient = optionalIngredient.get();
            }

            ingredients.add(ingredient);
        }

        UserEntity user = userRepository.findUserByUsername(username).get();
        CategoryEntity category = categoryRepository.getByNameIs(recipeAddServiceModel.getCategory());
        RecipeEntity recipe = modelMapper.map(recipeAddServiceModel, RecipeEntity.class);

        recipe.getIngredients().addAll(ingredients);
        recipe.setCategory(category);
        user.getCreatedRecipes().add(recipe);
        recipe.setAuthor(user);

        recipeRepository.save(recipe);
    }

    @Override
    public List<AllRecipesViewModel> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeEntity -> modelMapper.map(recipeEntity, AllRecipesViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDetailsViewModel findRecipeById(Long id) {
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Recipe not found with id: " + id));
        return modelMapper.map(recipeEntity, RecipeDetailsViewModel.class);
    }

    @Override
    public List<RecipeDetailsViewModel> getAllRecipesDetails() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeEntity -> modelMapper.map(recipeEntity, RecipeDetailsViewModel.class))
                .collect(Collectors.toList());
    }

}
