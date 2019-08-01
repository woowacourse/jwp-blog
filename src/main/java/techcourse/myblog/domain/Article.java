package techcourse.myblog.domain;

import techcourse.myblog.exception.InvalidAuthorException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String coverUrl;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    public Article() {
    }

    public Article(long id, String title, String coverUrl, String contents, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public Article update(Article updatedArticle) {
        this.title = updatedArticle.getTitle();
        this.coverUrl = updatedArticle.getCoverUrl();
        this.contents = updatedArticle.getContents();
        return this;
    }

    public void checkCorrespondingAuthor(User user) {
        if (!author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

    public long getArticleId() {
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
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
