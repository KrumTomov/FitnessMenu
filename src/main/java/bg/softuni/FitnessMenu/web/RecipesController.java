package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.exception.ObjectNotFoundException;
import bg.softuni.FitnessMenu.model.binding.RecipeAddBindingModel;
import bg.softuni.FitnessMenu.model.entity.enums.CategoryName;
import bg.softuni.FitnessMenu.model.service.RecipeAddServiceModel;
import bg.softuni.FitnessMenu.model.view.AllRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.FavoriteRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.MyRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.RecipeDetailsViewModel;
import bg.softuni.FitnessMenu.service.RecipeService;
import bg.softuni.FitnessMenu.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recipes")
public class RecipesController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public RecipesController(RecipeService recipeService, UserService userService, ModelMapper modelMapper) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public void addCategories(Model model) {
        model.addAttribute("categories", CategoryName.values());
    }

    @ModelAttribute
    public RecipeAddBindingModel recipeAddBindingModel() {
        return new RecipeAddBindingModel();
    }

    @ModelAttribute
    public AllRecipesViewModel allRecipesViewModel() {
        return new AllRecipesViewModel();
    }

    @ModelAttribute
    public MyRecipesViewModel myRecipesViewModel() {
        return new MyRecipesViewModel();
    }

    @ModelAttribute
    public FavoriteRecipesViewModel favoriteRecipesViewModel() {
        return new FavoriteRecipesViewModel();
    }

    @GetMapping("/add")
    public String addRecipe() {
        return "recipe-add";
    }

    @PostMapping("/add")
    public String addRecipePost(Principal principal,
                                @Valid RecipeAddBindingModel recipeAddBindingModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("recipeAddBindingModel", recipeAddBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.recipeAddBindingModel", bindingResult);
            return "redirect:add";
        }

        RecipeAddServiceModel recipeAddServiceModel = modelMapper.map(recipeAddBindingModel, RecipeAddServiceModel.class);
        recipeService.createRecipe(recipeAddServiceModel, principal.getName());
        return "redirect:/recipes/all";
    }

    @GetMapping("/all")
    public String getAllRecipes(Principal principal, Model model) {
        List<AllRecipesViewModel> recipes = recipeService.getAllRecipes();
        model.addAttribute("allRecipes", recipes);

        if (principal != null) {
            List<MyRecipesViewModel> myRecipes = userService.findCreatedRecipes(principal.getName());
            model.addAttribute("myRecipes", myRecipes);

            Set<FavoriteRecipesViewModel> favoriteRecipes = userService.findFavoriteRecipes(principal.getName());
            model.addAttribute("favoriteRecipes", favoriteRecipes);
            return "logged-in-recipes-all";
        }
        return "recipes-all";
    }

    @GetMapping("/details/{id}")
    public String detailsRecipe(@PathVariable Long id, Model model) {
        try {
            RecipeDetailsViewModel recipe = recipeService.findRecipeById(id);
            model.addAttribute("recipe", recipe);
            return "recipe-details";
        } catch (ObjectNotFoundException e) {
            return "404";
        }
    }

    @PostMapping("/favorite/{id}")
    public String addToFavorite(@PathVariable Long id, Principal principal) {
        userService.addToFavorite(id, principal.getName());
        return "redirect:/recipes/all";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, Principal principal) {
        recipeService.deleteRecipeById(id, principal.getName());
        return "redirect:/recipes/all";
    }

    @PostMapping("/remove/{id}")
    public String removeFromFavorite(@PathVariable Long id, Principal principal) {
        userService.removeFromFavorite(id, principal.getName());
        return "redirect:/recipes/all";
    }

}
