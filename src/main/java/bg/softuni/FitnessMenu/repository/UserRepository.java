package bg.softuni.FitnessMenu.repository;


import bg.softuni.FitnessMenu.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByUsername(String username);

    Optional<UserEntity> findUserByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE (u.lastLogin < :expirationTime AND u.isActive = true)")
    List<UserEntity> findInactiveUsers(@Param("expirationTime") LocalDateTime expirationTime);
}
