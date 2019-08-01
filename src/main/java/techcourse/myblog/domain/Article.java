package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.ArticleInputException;

import javax.persistence.*;
import java.net.URL;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Article {
    public static final String URL_FORMAT = "^(http(s))://.+\\..+/?.*";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;

    private String title;

    private String coverUrl;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_USER"), nullable = false)
    private User author;

    @Builder
    private Article(Long id, String title, String coverUrl, String contents, User author) {
        validateUrl(coverUrl);
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    private void validateUrl(String coverUrl) {
        if(!Pattern.matches(URL_FORMAT, coverUrl)) {
            throw new ArticleInputException("coverUrl이 잘 못 되었습니다.");
        }
    }
}
