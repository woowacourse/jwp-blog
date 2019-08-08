package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.application.service.exception.NotExistUserIdException;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = "id")
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Lob
    private String contents;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    protected Comment() {
    }

    public Comment(String contents, User user, Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public String modify(String contents, User user) {
        if (!this.user.equals(user)) {
            throw new NotExistUserIdException("작성자가 아닙니다.");
        }
        this.contents = contents;
        return this.contents;
    }
}
