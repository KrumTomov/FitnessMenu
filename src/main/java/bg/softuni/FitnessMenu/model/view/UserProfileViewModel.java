package bg.softuni.FitnessMenu.model.view;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileViewModel {

    private String username;

    @Email
    @NotBlank(message = "Email is required!")
    private String email;

    @Size(max = 70, message = "First name length must be max 20 characters!")
    private String firstName;

    @Size(max = 70, message = "Last name length must be max 20 characters!")
    private String lastName;

    @Positive(message = "Age must be positive number!")
    private int age;
}
