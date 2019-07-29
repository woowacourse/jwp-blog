package techcourse.myblog.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import techcourse.myblog.dto.ArticleSaveParams;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @Builder
    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public boolean isCoverUrl() {
        return StringUtils.isNotBlank(coverUrl);
    }

    public void update(ArticleSaveParams articleSaveParams) {
        this.title = articleSaveParams.getTitle();
        this.coverUrl = articleSaveParams.getCoverUrl();
        this.contents = articleSaveParams.getContents();
    }

    public void setAuthor(User user) {
        this.author = user;
    }
}
