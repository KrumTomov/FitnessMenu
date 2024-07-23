package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.model.view.InactiveUserViewModel;
import bg.softuni.FitnessMenu.model.view.RecipeDetailsViewModel;
import bg.softuni.FitnessMenu.model.view.UserViewModel;
import bg.softuni.FitnessMenu.service.RecipeService;
import bg.softuni.FitnessMenu.service.RoleService;
import bg.softuni.FitnessMenu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RecipeService recipeService, RoleService roleService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.roleService = roleService;
    }

    @ModelAttribute
    public void populateRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
    }

    @GetMapping("/all-users")
    public String getAllUsers(Model model) {
        List<UserViewModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users-all";
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/manage-roles/{id}")
    public String manageRoles(@PathVariable Long id, @RequestParam("roleIds") List<Long> roleIds) {
        userService.manageUserRoles(id, roleIds);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/all-recipes")
    public String getAllRecipes(Model model) {
        List<RecipeDetailsViewModel> recipes = recipeService.getAllRecipesDetails();
        model.addAttribute("recipes", recipes);
        return "admin-recipes-all";
    }

    @DeleteMapping("/delete-recipe/{id}")
    public String deleteRecipe(@PathVariable Long id, Principal principal) {
        recipeService.deleteRecipeById(id, principal.getName());
        return "redirect:/admin/all-recipes";
    }

    @GetMapping("/inactive-users")
    public String getInactiveUsers(Model model) {
        List<InactiveUserViewModel> inactiveUsers = userService.getInactiveUsers();
        model.addAttribute("users", inactiveUsers);
        return "admin-inactive-users";
    }

    @PostMapping("/activate-user/{id}")
    public String activateUser(@PathVariable Long id) {
        userService.activateUserById(id);
        return "redirect:/admin/inactive-users";
    }

}
