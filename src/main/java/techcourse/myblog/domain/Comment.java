package techcourse.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.dto.CommentDto;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contents;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id")
    private Article article;

    public Comment(String contents, User user, Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public Comment update(CommentDto commentDto, User user) {
        checkUpdateCondition(commentDto, user);
        this.contents = commentDto.getContents();
        return this;
    }

    public void checkMatchUser(User user) {
        if (this.user.isNotMatch(user)) {
            throw new IllegalArgumentException("not match user");
        }
    }

    public long getArticleId() {
        return this.article.getId();
    }

    private void checkUpdateCondition(CommentDto commentDto, User user) {
        checkMatchArticleId(commentDto.getArticleId());
        checkMatchUser(user);
    }

    private void checkMatchArticleId(long articleId) {
        if (this.article.isNotMatch(articleId)) {
            throw new IllegalArgumentException("not match article id");
        }
    }
}
