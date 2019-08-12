package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.service.exception.InvalidAuthorException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Comment extends DateTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User commenter;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private Comment() {
    }

    public Comment(String contents, Article article, User user) {
        checkContents(contents);
        checkUser(user);
        this.contents = contents;
        this.article = article;
        this.commenter = user;
    }

    private void checkUser(User user) {
        if (user == null) {
            throw new InvalidAuthorException("로그인 후 댓글을 작성할 수 있습니다.");
        }
    }

    private void checkContents(String contents) {
        if (contents == null) {
            throw new IllegalArgumentException("본문이 없습니다");
        }
    }

    public void checkCommenter(User user) {
        if (!this.commenter.equals(user)) {
            throw new InvalidAuthorException("작성자가 일지하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getCommenter() {
        return commenter;
    }

    public Article getArticle() {
        return article;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", createdDate=" + getCreatedDate() +
                ", modifiedDate=" + getModifiedDate() +
                ", commenter=" + commenter +
                ", article=" + article +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
