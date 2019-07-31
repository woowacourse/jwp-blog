package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.support.exception.MismatchAuthorException;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = "id")
@DynamicUpdate
public class Article {
    private static final int TITLE_LENGTH = 50;
    private static final String FK_FIELD_NAME = "author";
    private static final String FK_NAME = "fk_article_to_user";
    private static final String MISMATCH_AUTHOR_ERROR_MSG = "작성자가 아닙니다.";
    
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
    
    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public void update(Article article) {
        validateAuthor(article);
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
    }
    
    private void validateAuthor(Article article) {
        if (!article.author.equals(this.author)) {
            throw new MismatchAuthorException(MISMATCH_AUTHOR_ERROR_MSG);
        }
    }
}
