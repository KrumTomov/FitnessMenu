package bg.softuni.FitnessMenu.repository;


import bg.softuni.FitnessMenu.model.entity.RoleEntity;
import bg.softuni.FitnessMenu.model.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity getRoleByName(RoleName name);
}
