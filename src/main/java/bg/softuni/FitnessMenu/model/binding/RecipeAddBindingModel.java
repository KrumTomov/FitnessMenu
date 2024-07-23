package bg.softuni.FitnessMenu.model.binding;


import bg.softuni.FitnessMenu.model.entity.enums.CategoryName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Getter
@Setter
public class RecipeAddBindingModel {

    @NotBlank(message = "")
    @Length(min = 3,message = "Title length must be between 3 and 20 characters!")
    private String title;

    private String imageUrl;

    @NotBlank(message = "")
    @Length(min = 3, message = "Description length must be at least 3 characters!")
    private String description;

    @NotBlank(message = "You must add at least one ingredient!")
    private String ingredientNames;

    @NotNull
    private CategoryName category;

    @NotNull(message = "Preparation time must not be empty!")
    private LocalTime preparationTime;
}
