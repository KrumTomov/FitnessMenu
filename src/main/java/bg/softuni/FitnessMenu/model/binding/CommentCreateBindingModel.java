package bg.softuni.FitnessMenu.model.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateBindingModel {

    @NotBlank(message = "Comment cannot be empty!")
    @Size(min = 1)
    private String text;
}
