package techcourse.myblog.domain.category;

public class CategoryDto {
    private long categoryId;
    private String categoryName;

    public CategoryDto() {
    }

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getCategoryName());
    }

    public Category toCategory() {
        return new Category(categoryId, categoryName);
    }

    public CategoryDto(long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
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
