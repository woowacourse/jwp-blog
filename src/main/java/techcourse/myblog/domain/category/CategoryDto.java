package techcourse.myblog.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private long categoryId;
    private String categoryName;

    public CategoryDto() {
    }

    @Builder
    public CategoryDto(long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public Category toEntity() {
        return Category.builder()
                .categoryId(this.categoryId)
                .categoryName(this.categoryName)
                .build();
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
