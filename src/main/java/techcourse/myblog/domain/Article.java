package techcourse.myblog.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.dto.ArticleSaveRequestDto;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_article_to_user"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public void update(ArticleSaveRequestDto articleSaveRequestDto) {
        this.title = articleSaveRequestDto.getTitle();
        this.coverUrl = articleSaveRequestDto.getCoverUrl();
        this.contents = articleSaveRequestDto.getContents();
    }

    public boolean isAuthor(User user) {
        return author.equals(user);
    }

    public boolean isNotAuthor(User user) {
        return !isAuthor(user);
    }
}
