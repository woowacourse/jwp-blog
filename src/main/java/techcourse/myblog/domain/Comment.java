package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.support.exception.InvalidCommentException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 600)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;
    @CreationTimestamp
    private LocalDateTime createTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    public Comment(String contents, User writer, Article article) {
        validateContents(contents);
        this.contents = contents;
        this.writer = writer;
        this.article = article;
    }

    private void validateContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new InvalidCommentException("댓글은 비어있을 수 없습니다.");
        }
    }

    public void update(Comment comment) {
        if (comment == null) {
            throw new InvalidCommentException("댓글은 비어있을 수 없습니다.");
        }

        if (comment.article.equals(this.article) && comment.writer.equals(this.writer)) {
            this.contents = comment.contents;
            return;
        }

        throw new InvalidCommentException("댓글을 수정할 수 없습니다.");
    }
}
