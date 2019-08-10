package techcourse.myblog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import techcourse.myblog.domain.base.BaseEntity;
import techcourse.myblog.exception.ArticleAuthenticationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Article extends BaseEntity {
    private static final String DEFAULT_URL = "/images/default/bg.jpg";

    @Column(length = 40, nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
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
}