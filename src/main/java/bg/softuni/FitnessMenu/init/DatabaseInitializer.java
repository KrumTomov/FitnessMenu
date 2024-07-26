package bg.softuni.FitnessMenu.init;


import bg.softuni.FitnessMenu.service.CategoryService;
import bg.softuni.FitnessMenu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final CategoryService categoryService;

    @Autowired
    public DatabaseInitializer(RoleService roleService, CategoryService categoryService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.seedRoles();
        categoryService.seedCategories();
    }
}
