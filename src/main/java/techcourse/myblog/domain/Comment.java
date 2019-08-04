package techcourse.myblog.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.exception.IllegalCommentUpdateRequestException;

import javax.persistence.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @Builder
    public Comment(String contents, User user, Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public void update(String editedContents, User user) {
        if (isNotUser(user)) {
            log.debug("update comment request by illegal user id={}, comment id={}, editedContents={}"
                    , user.getId(), id, editedContents);
            throw new IllegalCommentUpdateRequestException();
        }

        this.contents = editedContents;
    }

    public boolean isUser(User user) {
        return this.user.equals(user);
    }

    public boolean isNotUser(User user) {
        return !isUser(user);
    }
}
