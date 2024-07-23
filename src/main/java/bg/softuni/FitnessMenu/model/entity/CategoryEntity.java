package bg.softuni.FitnessMenu.model.entity;


import bg.softuni.FitnessMenu.model.entity.enums.CategoryName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CategoryName name;

    public CategoryEntity(CategoryName name) {
        this.name = name;
    }
}
