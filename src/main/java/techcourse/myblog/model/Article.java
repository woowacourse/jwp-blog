package techcourse.myblog.model;

import lombok.*;
import techcourse.myblog.controller.dto.ArticleDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ARTICLE_ID")
    private Long id;

    @NonNull
    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @NonNull
    @Column(name = "COVER_URL")
    private String coverUrl;

    @NonNull
    @Column(name = "CONTENTS", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User user;

    public Article update(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
        this.user = article.user;

        return this;
    }
}
