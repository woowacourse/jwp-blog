package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.exception.InvalidAccessException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "coverUrl", nullable = false)
    private String coverUrl;

    @Lob
    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"), nullable = false)
    private User author;

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(Article articleToUpdate, long loginUserID) {
        isAuthor(loginUserID);
        this.title = articleToUpdate.title;
        this.coverUrl = articleToUpdate.coverUrl;
        this.contents = articleToUpdate.contents;
    }

    public void setAuthor(User persistUser) {
        this.author = persistUser;
    }

    public void isAuthor(long loginUserId) {
        if (loginUserId == id) {
            return;
        }

        throw new InvalidAccessException("작성자가 아닙니다.");
    }
}