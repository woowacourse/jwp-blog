package techcourse.myblog.domain.comment;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@DynamicInsert
@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String contents;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    public Comment() {
    }

    @Builder
    public Comment(long id, String contents, LocalDateTime createTime, User author, Article article) {
        this.id = id;
        this.contents = contents;
        this.createTime = createTime;
        this.author = author;
        this.article = article;
    }

    public void update(Comment comment) {
        this.contents = comment.getContents();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", createTime=" + createTime +
                ", author=" + author +
                ", article=" + article +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                Objects.equals(contents, comment.contents) &&
                Objects.equals(createTime, comment.createTime) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(article, comment.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, createTime, author, article);
    }
}
