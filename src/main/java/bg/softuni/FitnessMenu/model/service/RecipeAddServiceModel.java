package bg.softuni.FitnessMenu.model.service;


import bg.softuni.FitnessMenu.model.entity.enums.CategoryName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RecipeAddServiceModel {

    private String title;

    private String description;

    private String ingredientNames;

    private CategoryName category;

    private String imageUrl;

    private LocalTime preparationTime;
}
