package techcourse.myblog.domain.article;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Builder
    public ArticleDto(long id, String title, String coverUrl, String contents, long categoryId) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.categoryId = categoryId;
    }

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .coverUrl(article.getCoverUrl())
                .contents(article.getContents())
                .categoryId(article.getCategoryId()).build();
    }

    public Article toEntity() {
        return Article.builder()
                .id(this.id)
                .title(this.title)
                .coverUrl(this.coverUrl)
                .contents(this.contents)
                .categoryId(this.categoryId).build();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return id == that.id &&
                categoryId == that.categoryId &&
                Objects.equals(title, that.title) &&
                Objects.equals(coverUrl, that.coverUrl) &&
                Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents, categoryId);
    }
}
