package bg.softuni.FitnessMenu.service;



import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.service.UserServiceModel;
import bg.softuni.FitnessMenu.model.view.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    boolean registerUser(UserServiceModel userServiceModel);
    List<MyRecipesViewModel> findCreatedRecipes(String username);
    Set<FavoriteRecipesViewModel> findFavoriteRecipes(String username);
    void addToFavorite(Long id, String username);
    void removeFromFavorite(Long id, String username);
    Optional<UserEntity> getUserByUsername(String username);
    List<UserViewModel> getAllUsers();
    void deleteUser(Long id);
    void manageUserRoles(Long id, List<Long> roleIds);
    void incrementLoginCount(String username);
    void markUserAsActive(String username);
    void deactivateInactiveUsers();
    List<InactiveUserViewModel> getInactiveUsers();
    void activateUserById(Long id);
    void editProfileInfo(UserProfileViewModel userProfileViewModel, String username);
}
