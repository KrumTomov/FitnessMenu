package bg.softuni.FitnessMenu.service;



import bg.softuni.FitnessMenu.model.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    void seedRoles();

    List<RoleEntity> getAllRoles();
}
