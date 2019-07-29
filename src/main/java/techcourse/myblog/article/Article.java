package techcourse.myblog.article;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String coverUrl;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @Builder
    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;

    }

    public void checkCorrespondingAuthor(User user) {
        if (!this.author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }
}
