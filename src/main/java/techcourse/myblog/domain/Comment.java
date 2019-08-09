package techcourse.myblog.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import techcourse.myblog.domain.base.BaseEntity;
import techcourse.myblog.exception.CommentAuthenticationException;
import techcourse.myblog.service.dto.CommentRequestDto;

import javax.persistence.*;

@Entity
public class Comment extends BaseEntity {
    @Column(length = 100, nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "commenterId", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User commenter;

    @ManyToOne
    @JoinColumn(name = "articleId", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @JsonManagedReference
    private Article article;

    public Comment() {
    }

    public Comment(String contents, User commenter, Article article) {
        this.contents = contents;
        this.commenter = commenter;
        this.article = article;
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

    public void update(CommentRequestDto commentRequestDto, User user) {
        if (!isCommenter(user)) {
            throw new CommentAuthenticationException();
        }

        this.contents = commentRequestDto.getContents();
    }

    public boolean isCommenter(User user) {
        return user.equals(commenter);
    }
}
