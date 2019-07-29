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

    public String getComment() {
        return comment;
    }

    public String getAuthorName() {
        return author.getName();
    }
}
