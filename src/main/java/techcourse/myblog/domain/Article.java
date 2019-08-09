package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Size(max = 30)
    private String title;
    @Lob
    @Column(nullable = false)
    @Size(max = 50)
    private String coverUrl;
    @Column(nullable = false)
    @Size(max = 255)
    private String contents;
    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

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

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(Article article) {
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