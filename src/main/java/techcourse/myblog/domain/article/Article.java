package techcourse.myblog.domain.article;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.BaseEntity;
import techcourse.myblog.domain.InvalidAuthorException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Embedded
    Contents contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @Builder
    public Article(Contents contents, User author) {
        this.contents = contents;
        this.author = author;
    }

    public void update(Article article) {
        checkCorrespondingAuthor(article.getAuthor());
        this.contents = article.contents;
    }

    public void checkCorrespondingAuthor(User user) {
        if (!this.author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

}
