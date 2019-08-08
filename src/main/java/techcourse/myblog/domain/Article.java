package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.service.exception.MismatchAuthorException;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class Article extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public Article update(Article article) {
        matchAuthor(article.getAuthor());
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();

        return this;
    }

    public void matchAuthor(User user) {
        if (user == null || !user.equals(author)) {
            throw new MismatchAuthorException();
        }
    }
}
