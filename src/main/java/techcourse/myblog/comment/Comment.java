package techcourse.myblog.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @Column(nullable = false)
    private String contents;

    @Builder
    public Comment(User author, String contents) {
        this.author = author;
        this.contents = contents;
    }

    public void checkCorrespondingAuthor(User user) {
        if (!author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }
}
