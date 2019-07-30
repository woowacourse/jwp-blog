package techcourse.myblog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ARTICLE_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

//    @CreationTimestamp
//    @Column(name = "CURRENT_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, nullable = false)
//    private LocalDateTime createdTime;
//
//    @UpdateTimestamp
//    @Column(name = "UPDATE_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
//    private LocalDateTime updatedTime;

    @Column(name = "CONTENTS", nullable = false, columnDefinition = "TEXT")
    @NonNull
    private String contents;

    public Comment update(Comment comment) {
        this.author = comment.getAuthor();
        this.contents = comment.getContents();
        this.article = comment.getArticle();

        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
        if(!article.getComments().contains(this)) {
            article.getComments().add(this);
        }
    }
}
