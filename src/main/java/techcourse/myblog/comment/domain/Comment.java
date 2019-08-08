package techcourse.myblog.comment.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.comment.exception.CommentAuthenticationException;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.user.domain.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(name = "comment_contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "commenterId", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User commenter;

    @ManyToOne
    @JoinColumn(name = "articleId", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @JsonManagedReference
    private Article article;

    @CreatedDate
    private Date date;

    public Comment() {
    }

    public Comment(String contents, User commenter, Article article) {
        this.contents = contents;
        this.commenter = commenter;
        this.article = article;
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

    public Date getDate() {
        return date;
    }

    public Article getArticle() {
        return article;
    }

    public void update(CommentRequestDto commentRequestDto, User user) {
        if (!isCommenter(user)) {
            throw new CommentAuthenticationException();
        }

        this.contents = commentRequestDto.getContents();
    }

    public boolean isCommenter(User user) {
        return user.equals(commenter);
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
