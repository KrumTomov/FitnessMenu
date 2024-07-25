package bg.softuni.FitnessMenu.service;



import bg.softuni.FitnessMenu.model.service.CommentCreateServiceModel;
import bg.softuni.FitnessMenu.model.view.CommentViewModel;

import java.util.List;

public interface CommentService {

    List<CommentViewModel> getCommentsByRecipe(Long id);

    CommentViewModel createComment(CommentCreateServiceModel commentCreateServiceModel, Long recipeId, String username);

}
