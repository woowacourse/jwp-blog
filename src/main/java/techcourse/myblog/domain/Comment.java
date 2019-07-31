package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.support.exception.InvalidCommentException;
import techcourse.myblog.support.exception.MismatchAuthorException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = "id")
public class Comment {
    public static final String USER_FK_FIELD_NAME = "writer";
    public static final String USER_FK_NAME = "fk_comment_to_user";
    public static final String ARTICLE_FK_FILED_NAME = "article";
    public static final String ARTICLE_FK_NAME = "fk_comment_to_article";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = USER_FK_FIELD_NAME, foreignKey = @ForeignKey(name = USER_FK_NAME))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @ManyToOne
    @JoinColumn(name = ARTICLE_FK_FILED_NAME, foreignKey = @ForeignKey(name = ARTICLE_FK_NAME))
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
            throw new InvalidCommentException();
        }
    }

    public void update(Comment comment) {
        validateComment(comment);
        validateArticleAndAuthor(comment);
    }
    
    private void validateComment(Comment comment) {
        if (comment == null) {
            throw new InvalidCommentException();
        }
    }
    
    private void validateArticleAndAuthor(Comment comment) {
        if (comment.article.equals(this.article) && comment.writer.equals(this.writer)) {
            this.contents = comment.contents;
            return;
        }
        
        throw new MismatchAuthorException();
    }
}
