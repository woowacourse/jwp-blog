package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public void update(Comment updatedComment) {
        this.contents = updatedComment.contents;
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
