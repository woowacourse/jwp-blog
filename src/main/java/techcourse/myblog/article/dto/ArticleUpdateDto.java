package techcourse.myblog.article.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;

@Getter
@Setter
public class ArticleUpdateDto {
    private String title;
    private String coverUrl;
    private String contents;

    public Article toArticle(long articleId) {
        return Article.builder()
                .id(articleId)
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .build();
    }
}
