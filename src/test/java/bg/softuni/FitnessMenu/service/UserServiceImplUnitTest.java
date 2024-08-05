package bg.softuni.FitnessMenu.service;


import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.RoleEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.entity.enums.RoleName;
import bg.softuni.FitnessMenu.model.service.UserServiceModel;
import bg.softuni.FitnessMenu.model.view.FavoriteRecipesViewModel;
import bg.softuni.FitnessMenu.model.view.MyRecipesViewModel;
import bg.softuni.FitnessMenu.repository.RecipeRepository;
import bg.softuni.FitnessMenu.repository.RoleRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserServiceModel userServiceModel;
    private UserEntity mockUserEntity;

    @BeforeEach
    public void setUp() {
        userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("testUser");
        userServiceModel.setEmail("test@example.com");
        userServiceModel.setPassword("password");

        mockUserEntity = new UserEntity();
        mockUserEntity.setUsername("testUser");
        mockUserEntity.setEmail("test@example.com");
        mockUserEntity.setPassword("encodedPassword");
    }

    @Test
    public void testRegisterUserSuccessful() {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(userServiceModel, UserEntity.class)).thenReturn(mockUserEntity);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Act
        boolean isRegistered = userService.registerUser(userServiceModel);

        // Assert
        assertTrue(isRegistered, "Expected registration to be successful");
        assertEquals("encodedPassword", mockUserEntity.getPassword(), "Expected the password to be encoded");
    }

    @Test
    public void testRegisterUserWithTakenEmail() {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(new UserEntity()));

        // Act
        boolean isRegistered = userService.registerUser(userServiceModel);

        // Assert
        assertFalse(isRegistered);
    }

    @Test
    public void testRegisterUserWithTakenUsername() {
        // Arrange
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.registerUser(userServiceModel);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFirstUserIsAdministrator() {
        // Arrange
        when(userRepository.count()).thenReturn(0L);
        when(modelMapper.map(userServiceModel, UserEntity.class)).thenReturn(mockUserEntity);
        when(roleRepository.getRoleByName(RoleName.ADMINISTRATOR)).thenReturn(new RoleEntity(RoleName.ADMINISTRATOR));

        // Act
        userService.registerUser(userServiceModel);

        // Assert
        assertTrue(mockUserEntity.getRoles().contains(roleRepository.getRoleByName(RoleName.ADMINISTRATOR)),
                "Expected the user to have the ADMINISTRATOR role.");
        assertEquals(1, mockUserEntity.getRoles().size(), "Expected exactly one role to be assigned.");
    }

    @Test
    public void testRegisterUserWithUserRole() {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(userServiceModel, UserEntity.class)).thenReturn(mockUserEntity);
        when(userRepository.count()).thenReturn(1L);
        when(roleRepository.getRoleByName(RoleName.USER)).thenReturn(new RoleEntity(RoleName.USER));

        // Act
        boolean isRegistered = userService.registerUser(userServiceModel);

        // Assert
        assertTrue(isRegistered, "Expected registration to be successful");
        assertEquals(1, mockUserEntity.getRoles().size(), "Expected exactly one role to be assigned");
        assertTrue(mockUserEntity.getRoles().contains(roleRepository.getRoleByName(RoleName.USER)),
                "Expected the user to have the USER role");
    }

    @Test
    public void testFindCreatedRecipes() {
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);
        recipe1.setAuthor(mockUserEntity);

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);
        recipe2.setAuthor(mockUserEntity);

        List<RecipeEntity> mockRecipes = Arrays.asList(recipe1, recipe2);

        when(recipeRepository.findAll()).thenReturn(mockRecipes);

        MyRecipesViewModel mappedRecipe1 = new MyRecipesViewModel();
        mappedRecipe1.setId(1L);

        MyRecipesViewModel mappedRecipe2 = new MyRecipesViewModel();
        mappedRecipe2.setId(2L);

        when(modelMapper.map(recipe1, MyRecipesViewModel.class)).thenReturn(mappedRecipe1);
        when(modelMapper.map(recipe2, MyRecipesViewModel.class)).thenReturn(mappedRecipe2);


        List<MyRecipesViewModel> result = userService.findCreatedRecipes(userServiceModel.getUsername());


        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    public void testFindFavoriteRecipes() {
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);

        Set<RecipeEntity> mockFavoriteRecipes = new HashSet<>();
        mockFavoriteRecipes.add(recipe1);
        mockFavoriteRecipes.add(recipe2);

        mockUserEntity.setFavoriteRecipes(mockFavoriteRecipes);

        when(userRepository.findUserByUsername(userServiceModel.getUsername())).thenReturn(Optional.of(mockUserEntity));

        FavoriteRecipesViewModel mappedRecipe1 = new FavoriteRecipesViewModel();
        mappedRecipe1.setId(1L);

        FavoriteRecipesViewModel mappedRecipe2 = new FavoriteRecipesViewModel();
        mappedRecipe2.setId(2L);

        when(modelMapper.map(recipe1, FavoriteRecipesViewModel.class)).thenReturn(mappedRecipe1);
        when(modelMapper.map(recipe2, FavoriteRecipesViewModel.class)).thenReturn(mappedRecipe2);


        Set<FavoriteRecipesViewModel> result = userService.findFavoriteRecipes(userServiceModel.getUsername());


        assertEquals(2, result.size(), "Expected two favorite recipes.");
    }

    @Test
    public void testAddToFavorite() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(1L);

        when(userRepository.findUserByUsername(userServiceModel.getUsername())).thenReturn(Optional.of(mockUserEntity));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));


        userService.addToFavorite(1L, userServiceModel.getUsername());


        assertTrue(mockUserEntity.getFavoriteRecipes().contains(recipe), "Expected recipe to be in the user's favorite recipes");
    }

    @Test
    public void testRemoveFromFavorite() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(1L);

        Set<RecipeEntity> favoriteRecipes = new HashSet<>();
        favoriteRecipes.add(recipe);
        mockUserEntity.setFavoriteRecipes(favoriteRecipes);

        when(userRepository.findUserByUsername(userServiceModel.getUsername())).thenReturn(Optional.of(mockUserEntity));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));


        userService.removeFromFavorite(1L, userServiceModel.getUsername());


        assertFalse(mockUserEntity.getFavoriteRecipes().contains(recipe), "Expected recipe to be removed from the user's favorite recipes");
    }
    @Test
    public void testManageUserRoles() {
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);

        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);

        List<Long> roleIds = Arrays.asList(1L, 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));
        when(roleRepository.findAllById(roleIds)).thenReturn(Arrays.asList(role1, role2));

        userService.manageUserRoles(1L, roleIds);

        assertEquals(new HashSet<>(Arrays.asList(role1, role2)), mockUserEntity.getRoles());
    }
}
