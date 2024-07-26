package bg.softuni.FitnessMenu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserDisabledException extends UsernameNotFoundException {

    public UserDisabledException(String username) {
        super("User with username " + username + " is disabled. Please contact support.");
    }
}
