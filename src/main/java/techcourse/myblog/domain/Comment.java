package techcourse.myblog.domain;

import techcourse.myblog.exception.InvalidAuthorException;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    public Comment() {
    }

    public Comment(String contents, User author) {
        this.contents = contents;
        this.author = author;
    }

    public Comment update(Comment updatedComment) {
        this.contents = updatedComment.contents;
        return this;
    }

    public void checkCorrespondingAuthor(User user) {
        if (!author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

    public long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public String getAuthor() {
        return author.getName();
    }
}
