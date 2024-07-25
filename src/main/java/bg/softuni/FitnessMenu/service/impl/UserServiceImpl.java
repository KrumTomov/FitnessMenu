package bg.softuni.FitnessMenu.service.impl;

import bg.softuni.FitnessMenu.model.entity.CommentEntity;
import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.RoleEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.entity.enums.RoleName;
import bg.softuni.FitnessMenu.model.service.UserServiceModel;
import bg.softuni.FitnessMenu.model.view.*;
import bg.softuni.FitnessMenu.repository.CommentRepository;
import bg.softuni.FitnessMenu.repository.RecipeRepository;
import bg.softuni.FitnessMenu.repository.RoleRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends CommonServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RecipeRepository recipeRepository,
            CommentRepository commentRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper) {
        super(userRepository, recipeRepository, commentRepository);
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {
        Optional<UserEntity> userByUsername = userRepository.findUserByUsername(userServiceModel.getUsername());
        Optional<UserEntity> userByEmail = userRepository.findUserByEmail(userServiceModel.getEmail());
        if (userByUsername.isPresent() || userByEmail.isPresent()) {
            return false;
        }

        UserEntity userEntity = modelMapper.map(userServiceModel, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        if (userRepository.count() == 0) {
            userEntity.getRoles().add(roleRepository.getRoleByName(RoleName.ADMINISTRATOR));
        } else {
            userEntity.getRoles().add(roleRepository.getRoleByName(RoleName.USER));
        }

        userRepository.save(userEntity);
        return true;
    }

    @Override
    public List<MyRecipesViewModel> findCreatedRecipes(String username) {
        List<RecipeEntity> recipes = recipeRepository.findAll();
        return recipes.stream()
                .filter(recipeEntity -> recipeEntity.getAuthor().getUsername().equals(username))
                .map(recipeEntity -> modelMapper.map(recipeEntity, MyRecipesViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Set<FavoriteRecipesViewModel> findFavoriteRecipes(String username) {
        UserEntity user = userRepository.findUserByUsername(username).get();
        Set<RecipeEntity> favoriteRecipes = user.getFavoriteRecipes();
        return favoriteRecipes.stream()
                .map(recipeEntity -> modelMapper.map(recipeEntity, FavoriteRecipesViewModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void addToFavorite(Long id, String username) {
        UserEntity user = userRepository.findUserByUsername(username).get();
        RecipeEntity recipe = recipeRepository.findById(id).get();
        Set<RecipeEntity> favoriteRecipes = user.getFavoriteRecipes();

        favoriteRecipes.add(recipe);
        user.setFavoriteRecipes(favoriteRecipes);

        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeFromFavorite(Long id, String username) {
        UserEntity user = userRepository.findUserByUsername(username).get();
        RecipeEntity recipe = recipeRepository.findById(id).get();

        user.getFavoriteRecipes().remove(recipe);
        userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public List<UserViewModel> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userEntity -> modelMapper.map(userEntity, UserViewModel.class)).toList();
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).get();

        List<CommentEntity> comments = commentRepository.findAllByAuthor_Id(user.getId());
        commentRepository.deleteAll(comments);

        List<RecipeEntity> createdRecipes = user.getCreatedRecipes();
        for (int i = 0; i < createdRecipes.size(); i++) {
            RecipeEntity recipe = createdRecipes.get(i);
            deleteRecipeById(recipe.getId(), user.getUsername());
        }

        user.setFavoriteRecipes(new HashSet<>());
        user.setRoles(null);
        userRepository.deleteById(id);
    }

    @Override
    public void manageUserRoles(Long id, List<Long> roleIds) {
        UserEntity user = userRepository.findById(id).get();
        List<RoleEntity> roles = roleRepository.findAllById(roleIds);

        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
    }

    @Override
    public void incrementLoginCount(String username) {
        Optional<UserEntity> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setLoginCount(user.getLoginCount() + 1);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public void markUserAsActive(String username) {
        Optional<UserEntity> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setActive(true);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public void deactivateInactiveUsers() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(365);
        List<UserEntity> inactiveUsers = userRepository.findInactiveUsers(expirationTime);

        for (int i = 0; i < inactiveUsers.size(); i++) {
            UserEntity user = inactiveUsers.get(i);
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    public List<InactiveUserViewModel> getInactiveUsers() {
        List<UserEntity> inactiveUsers = userRepository.findAll().stream().filter(userEntity -> !userEntity.isActive()).toList();
        return inactiveUsers.stream().map(userEntity -> modelMapper.map(userEntity, InactiveUserViewModel.class)).toList();
    }

    @Override
    public void activateUserById(Long id) {
        UserEntity user = userRepository.findById(id).get();
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void editProfileInfo(UserProfileViewModel userProfileViewModel, String username) {
        UserEntity user = userRepository.findUserByUsername(username).get();

        user.setFirstName(userProfileViewModel.getFirstName());
        user.setEmail(userProfileViewModel.getEmail());
        user.setLastName(userProfileViewModel.getLastName());
        user.setAge(userProfileViewModel.getAge());

        userRepository.save(user);
    }

}
