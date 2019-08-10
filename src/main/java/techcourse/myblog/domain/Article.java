package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotMatchArticleAuthorException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    private Article() {
    }

    public static class ArticleBuilder {
        private String title;
        private String coverUrl;
        private String contents;
        private User author;

        public ArticleBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleBuilder coverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
            return this;
        }

        public ArticleBuilder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public ArticleBuilder author(User author) {
            this.author = author;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

    public Article(ArticleBuilder articleBuilder) {
        this.title = articleBuilder.title;
        this.coverUrl = articleBuilder.coverUrl;
        this.contents = articleBuilder.contents;
        this.author = articleBuilder.author;
    }

    public Article(String title, String coverUrl, String contents, User author) {
        this(null, title, coverUrl, contents, author);
    }

    public Article(Long id, String title, String coverUrl, String contents, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public void modify(ArticleDto article, User user) {
        if (isNotAuthor(user)) {
            throw new NotMatchArticleAuthorException("너는 이 글에 작성자가 아니다. 꺼져라!");
        }
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
    }

    public boolean isNotAuthor(User user) {
        return !author.equals(user);
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

    public User getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Article article = (Article) object;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}