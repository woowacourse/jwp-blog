package techcourse.myblog.domain.comment;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.BaseTimeEntity;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.function.Function;

@Entity
public class Comment extends BaseTimeEntity {
    private static final int COMMENT_LENGTH = 100;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = COMMENT_LENGTH)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "FK_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false, foreignKey = @ForeignKey(name = "FK_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private Comment() {

    }

    public Comment(String comment, User author, Article article) {
        this.comment = comment;
        this.author = author;
        this.article = article;
    }

    public boolean matchAuthor(User author) {
        return this.author.equals(author);
    }

    public void updateComment(Comment paramComment) {
        if (paramComment.matchAuthor(this.author)) {
            this.comment = paramComment.comment;
        }
    }

    public int subtractionOfDays(Function<Comment, Integer> supplier) {
        return supplier.apply(this);
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
