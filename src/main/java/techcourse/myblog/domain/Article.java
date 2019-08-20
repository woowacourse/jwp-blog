package techcourse.myblog.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.service.exception.InvalidAuthorException;

import javax.persistence.*;

@Entity
public class Article extends DateTime {
    private static final Logger log = LoggerFactory.getLogger(Article.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, length = 300)
    private String coverUrl;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_user"))
    private User author;

    private Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
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

    public void updateArticle(Article article, User user) {
        checkAuthor(user);
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public void checkAuthor(User user) {
        if (!this.author.equals(user)) {
            log.warn("author: {}, user: {}", this.author, user);
            throw new InvalidAuthorException("작성자가 일치하지 않습니다.");
        }
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", author=" + author +
                '}';
    }
}
