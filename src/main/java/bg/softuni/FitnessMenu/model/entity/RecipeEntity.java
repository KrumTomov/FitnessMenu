package bg.softuni.FitnessMenu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
public class RecipeEntity extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 3)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipes_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    private List<IngredientEntity> ingredients = new ArrayList<>();

    @ManyToOne
    private CategoryEntity category;

    @ManyToOne
    private UserEntity author;

    @Column(nullable = false)
    private LocalTime preparationTime;

    @OneToMany(mappedBy = "recipe", targetEntity = CommentEntity.class)
    private List<CommentEntity> comments;

}
