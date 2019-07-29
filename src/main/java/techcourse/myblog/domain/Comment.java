package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private User writer;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

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
        this.contents = comment.contents;
    }
}
