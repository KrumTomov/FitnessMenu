package bg.softuni.FitnessMenu.model.view;


import bg.softuni.FitnessMenu.model.entity.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserViewModel {

    private Long id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private int age;

    private Set<RoleEntity> roles;
}
