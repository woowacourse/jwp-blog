package techcourse.myblog.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
@EntityListeners(value = { AuditingEntityListener.class })
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 1000)
    private String comment;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_COMMENTER"), name = "commenter")
    private User commenter;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ARTICLE"), name = "article")
    private Article article;
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(String comment, User commenter, Article article) {
        this.comment = comment;
        this.commenter = commenter;
        this.article = article;
    }

    public void update(Comment comment) {
        this.comment = comment.getComment();
    }

    public boolean writtenBy(User user) {
        return commenter.equals(user);
    }
}
