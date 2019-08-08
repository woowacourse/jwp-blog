package techcourse.myblog.domain.comment;

import techcourse.myblog.domain.BaseTimeEntity;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.function.Function;

@Entity
public class Comment extends BaseTimeEntity {
    private static final int COMMENT_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = COMMENT_LENGTH)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "FK_comment_to_user"))
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "FK_comment_to_article"))
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
        if (this.author.equals(paramComment.author)) {
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
