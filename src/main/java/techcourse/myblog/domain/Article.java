package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;
    private String coverUrl;
    private String contents;

    public static Article of(ArticleDto articleDto) {
        Article article = new Article();
        article.title = articleDto.getTitle();
        article.coverUrl = articleDto.getCoverUrl();
        article.contents = articleDto.getContents();
        return article;
    }

    public void updateArticle(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.coverUrl = articleDto.getCoverUrl();
        this.contents = articleDto.getContents();
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleId, article.articleId) &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, coverUrl, contents);
    }
}
