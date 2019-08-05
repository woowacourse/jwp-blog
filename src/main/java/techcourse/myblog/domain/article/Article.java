package techcourse.myblog.domain.article;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ArticleDetails articleDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    public Article(ArticleDetails articleDetails, User author) {
        this.articleDetails = articleDetails;
        this.author = author;
    }

    public boolean isWrittenBy(User user) {
        return author.equals(user);
    }

    public void update(ArticleDetails articleDetails) {
        this.articleDetails = articleDetails;
    }
}