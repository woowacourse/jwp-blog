package techcourse.myblog.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.BaseEntity;
import techcourse.myblog.domain.InvalidAuthorException;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @Column(nullable = false)
    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    @Builder
    public Comment(User author, String contents, Article article) {
        this.author = author;
        this.contents = contents;
        this.article = article;
    }

    private void checkCorrespondingAuthor(User user) {
        if (!author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

    public void update(Comment updateComment) {
        checkCorrespondingAuthor(updateComment.author);
        this.contents = updateComment.contents;
    }

    public void checkAvailableUserForDelete(User user) {
        checkCorrespondingAuthor(user);
    }
}
