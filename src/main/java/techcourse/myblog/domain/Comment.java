package techcourse.myblog.domain;

import org.springframework.lang.NonNull;
import techcourse.myblog.exception.UnauthenticatedUserException;

import javax.persistence.*;

@Entity
public class Comment {
    public Comment() {
    }

    public Comment(String contents, Article article, User author) {
        this.contents = contents;
        this.article = article;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Article getArticle() {
        return article;
    }

    public User getAuthor() {
        return author;
    }

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    @NonNull
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @NonNull
    private User author;

    public void update(Comment comment) {
        this.contents = comment.contents;
    }

    public void validate(User user) {
        if (author.equals(user) == false) {
            throw new UnauthenticatedUserException();
        }
    }
}
