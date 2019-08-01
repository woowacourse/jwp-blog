package techcourse.myblog.application.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.Article;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(Long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public static ArticleDto of(Article article) {
        return new ArticleDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents()
                );
    }
}
