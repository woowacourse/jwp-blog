package techcourse.myblog.domain;

import techcourse.myblog.web.dto.ArticleRequestDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Article {
    private static long NEXT_ID = 1;

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public static Article of(long id, String title, String coverUrl, String contents) {
        Article newArticle = new Article();
        newArticle.id = id;
        newArticle.title = title;
        newArticle.coverUrl = coverUrl;
        newArticle.contents = contents;
        return newArticle;
    }

    public static Article of(String title, String coverUrl, String content) {
        return of(NEXT_ID++, title, coverUrl, content);
    }

    public static Article from(ArticleRequestDto dto) {
        Article newArticle = new Article();
        newArticle.id = NEXT_ID++;
        newArticle.title = dto.getTitle();
        newArticle.coverUrl = dto.getCoverUrl();
        newArticle.contents = dto.getContents();
        return newArticle;
    }

    public Long getId() {
        return id;
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

    public void update(Article article) {
        title = article.getTitle();
        coverUrl = article.coverUrl;
        contents = article.contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) &&
            Objects.equals(title, article.title) &&
            Objects.equals(coverUrl, article.coverUrl) &&
            Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
