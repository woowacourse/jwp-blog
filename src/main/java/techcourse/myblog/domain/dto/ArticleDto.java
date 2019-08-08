package techcourse.myblog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long articleId;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article toEntity(User author) {
        return Article.builder()
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .author(author)
                .build();
    }

    public static ArticleDto toArticleDto(Article article) {
        return new ArticleDto(article.getArticleId(), article.getTitle(),
                article.getCoverUrl(), article.getContents());
    }
}
