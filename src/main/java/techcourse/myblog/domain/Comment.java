package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @Column(nullable = false)
    private String contents;

    public Comment() {
    }

    public Comment(User author, String contents) {
        this.author = author;
        this.contents = contents;
    }

    public void update(Comment updatedComment) {
        this.contents = updatedComment.contents;
    }
}
