package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
public class Comment {
    private static final int COMMENT_LENGTH = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = COMMENT_LENGTH)
    private String comment;

    @ManyToOne
    private User author;

    @ManyToOne
    private Article article;

    private Comment() {

    }

    public Comment(String comment, User author, Article article) {
        this.comment = comment;
        this.author = author;
        this.article = article;
    }

    public boolean matchAuthorId(Long userId) {
        return this.author.matchId(userId);
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public Long getAuthorId() {
        return author.getId();
    }

    public String getAuthorName() {
        return author.getName();
    }
}
