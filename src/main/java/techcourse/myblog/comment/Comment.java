package techcourse.myblog.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.date.Date;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @Column(nullable = false)
    @Lob
    private String contents;

//    @ManyToOne
//    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
//    @Column(nullable = false)
//    private Article article;

    @Builder
    public Comment(User author, String contents) {
        this.author = author;
        this.contents = contents;
    }

    private void checkCorrespondingAuthor(User user) {
        if (!author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

    public void update(Comment updateComment, User user) {
        checkCorrespondingAuthor(user);
        this.contents = updateComment.contents;
    }

    public void checkAvailableUserForDelete(User user) {
        checkCorrespondingAuthor(user);
    }
}
