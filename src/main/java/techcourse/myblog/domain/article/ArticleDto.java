package techcourse.myblog.domain.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;
    private long categoryId;

    public ArticleDto() {
    }

    public ArticleDto(long id, String title, String coverUrl, String contents, long categoryId) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.categoryId = categoryId;
    }

    public static ArticleDto from(Article article) {
        return new ArticleDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents(),
                article.getCategoryId());
    }

    public Article toEntity() {
        return new Article(this.id, this.title, this.coverUrl, this.contents, this.categoryId);
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
