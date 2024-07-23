package bg.softuni.FitnessMenu.model.binding;


import bg.softuni.FitnessMenu.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Password must match!"
)
public class UserRegisterBindingModel {

    @NotBlank(message = "Email is required!")
    @Email
    private String email;

    @Length(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    @NotBlank(message = "Username is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    @Length(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    @NotBlank(message = "Confirm Password is required!")
    @Length(min = 3, max = 20, message = "Confirm Password length must be between 3 and 20 characters!")
    private String confirmPassword;

    @Size(max = 70, message = "First name length must be max 20 characters!")
    private String firstName;

    @Size(max = 70, message = "Last name length must be max 20 characters!")
    private String lastName;

    @Positive(message = "Age must be positive number!")
    private int age;
}
