package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import techcourse.myblog.support.exception.MismatchAuthorException;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = "id")
@DynamicUpdate
public class Article {
    private static final int TITLE_LENGTH = 50;
    private static final String FK_FIELD_NAME = "author";
    private static final String FK_NAME = "fk_article_to_user";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    @Lob
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = FK_FIELD_NAME, foreignKey = @ForeignKey(name = FK_NAME))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @CreationTimestamp
    private LocalDateTime createTimeAt;

    @UpdateTimestamp
    private LocalDateTime updateTimeAt;
    
    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public void update(Article article) {
        validateAuthor(article);
        title = article.title;
        coverUrl = article.coverUrl;
        contents = article.contents;
    }
    
    private void validateAuthor(Article article) {
        if (matchAuthor(article)) {
            return;
        }
        throw new MismatchAuthorException();
    }

    private boolean matchAuthor(Article article) {
        return article.author.equals(this.author);
    }
}
