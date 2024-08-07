package bg.softuni.FitnessMenu.repository;


import bg.softuni.FitnessMenu.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByRecipe_Id(Long id);

    List<CommentEntity> findAllByAuthor_Id(Long id);

}
