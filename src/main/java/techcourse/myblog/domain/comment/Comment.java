package techcourse.myblog.domain.comment;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime modfiedDate;

    @Column(nullable = false)
    private String contents;

    public Comment(Long id, String contents, User user, Article article) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    public boolean isWrittenBy(User other) {
        return user.equals(other);
    }

    public void update(CommentDto.Update commentDto) {
        contents = commentDto.getContents();
    }
}
