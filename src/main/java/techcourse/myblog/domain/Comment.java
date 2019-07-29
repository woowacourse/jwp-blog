package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_COMMENTER"), name = "commenter")
    private User commenter;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ARTICLE"), name = "article")
    private Article article;

    private LocalDateTime createdAt;

    public Comment(String comment) {
        this.comment = comment;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
