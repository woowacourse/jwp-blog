package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.service.MismatchAuthorException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @CreationTimestamp
    private LocalDateTime createTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    private Article() {
    }

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
