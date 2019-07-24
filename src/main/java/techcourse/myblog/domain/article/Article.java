package techcourse.myblog.domain.article;

import techcourse.myblog.exception.ArticleToUpdateNotFoundException;

import javax.persistence.*;
import java.util.Objects;


@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    private String contents;

    public Article() {
    }

    public Article(final String title, final String coverUrl, final String contents) {
        this.title = Objects.requireNonNull(title);
        this.coverUrl = Objects.requireNonNull(coverUrl);
        this.contents = Objects.requireNonNull(contents);
    }

    public void update(final Article article) {
        if (Objects.isNull(article)) {
            throw new ArticleToUpdateNotFoundException("업데이트 해야할 게시글이 없습니다.");
        }
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
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
