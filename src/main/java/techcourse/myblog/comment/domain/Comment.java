package techcourse.myblog.comment.domain;

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
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "commenterId", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
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
        System.out.println("commenter " + commenter.getId());
        System.out.println("user " + user.getId());
        System.out.println("user vs comment object" + user.equals(commenter));
        System.out.println("user id vs comment id" + user.getId().equals(commenter.getId()));
        return user.equals(commenter);
        //return user.getId().equals(id);
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
