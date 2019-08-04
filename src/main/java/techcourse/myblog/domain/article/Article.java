package techcourse.myblog.domain.article;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ArticleVo articleVo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    public Article(ArticleVo articleVo, User author) {
        this.articleVo = articleVo;
        this.author = author;
    }

    public boolean isWrittenBy(User user) {
        return author.equals(user);
    }

    public void update(ArticleVo articleVo) {
        this.articleVo = articleVo;
    }
}