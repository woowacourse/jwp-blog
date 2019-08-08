package techcourse.myblog.article.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import techcourse.myblog.article.exception.ArticleAuthenticationException;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.user.domain.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Article {
    private static final String DEFAULT_URL = "/images/default/bg.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(name = "article_title", nullable = false, length = 20)
    private String title;

    @Lob
    @Column(name = "article_contents")
    private String contents;

    @Column(name = "article_cover_url")
    private String coverUrl;

    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Comment> comments;

    public Article() {
    }

    public Article(String title, String contents, String coverUrl, User author) {
        this(title, contents, coverUrl);
        this.author = author;
        this.comments = new ArrayList<>();
    }

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = getDefaultUrl(coverUrl);
    }

    private String getDefaultUrl(String coverUrl) {
        if (coverUrl.isEmpty()) {
            return DEFAULT_URL;
        }
        return coverUrl;
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

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComments(Comment comment) {
        comments.add(comment);
    }

    public void update(Article modifiedArticle, User user) {
        if (!isAuthor(user)) {
            throw new ArticleAuthenticationException();
        }

        this.title = modifiedArticle.title;
        this.contents = modifiedArticle.contents;
        this.coverUrl = modifiedArticle.coverUrl;
    }

    public boolean isAuthor(User user) {
        return user.equals(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}