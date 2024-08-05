package bg.softuni.FitnessMenu.service;


import bg.softuni.FitnessMenu.model.entity.CommentEntity;
import bg.softuni.FitnessMenu.model.entity.RecipeEntity;
import bg.softuni.FitnessMenu.model.entity.UserEntity;
import bg.softuni.FitnessMenu.model.service.CommentCreateServiceModel;
import bg.softuni.FitnessMenu.model.view.CommentViewModel;
import bg.softuni.FitnessMenu.repository.CommentRepository;
import bg.softuni.FitnessMenu.repository.RecipeRepository;
import bg.softuni.FitnessMenu.repository.UserRepository;
import bg.softuni.FitnessMenu.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplUnitTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    public void testGetCommentsByRecipe() {

        Long recipeId = 1L;
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);

        when(commentRepository.findAllByRecipe_Id(recipeId)).thenReturn(List.of(commentEntity));
        when(modelMapper.map(commentEntity, CommentViewModel.class)).thenReturn(new CommentViewModel());


        List<CommentViewModel> result = commentService.getCommentsByRecipe(recipeId);


        assertEquals(1, result.size());
    }

    @Test
    public void testCreateComment() {

        Long recipeId = 1L;
        String username = "testUser";
        CommentCreateServiceModel commentCreateServiceModel = new CommentCreateServiceModel();
        CommentEntity commentEntity = new CommentEntity();

        when(modelMapper.map(commentCreateServiceModel, CommentEntity.class)).thenReturn(commentEntity);
        when(userRepository.findUserByUsername(username)).thenReturn(java.util.Optional.of(new UserEntity()));
        when(recipeRepository.findById(recipeId)).thenReturn(java.util.Optional.of(new RecipeEntity()));
        when(modelMapper.map(commentEntity, CommentViewModel.class)).thenReturn(new CommentViewModel());


        CommentViewModel result = commentService.createComment(commentCreateServiceModel, recipeId, username);


        assertEquals(commentEntity.getId(), result.getId());
    }


}