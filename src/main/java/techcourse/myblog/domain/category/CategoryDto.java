package techcourse.myblog.domain.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private long categoryId;
    private String categoryName;

    public CategoryDto() {
    }

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getCategoryName());
    }

    public Category toEntity() {
        return new Category(categoryId, categoryName);
    }

    public CategoryDto(long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
