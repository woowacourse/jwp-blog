package techcourse.myblog.service.dto;

import lombok.Getter;
import techcourse.myblog.domain.Article;

@Getter
public class ArticleDto {
    private String title;
    private String coverUrl;
    private String contents;
    
    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article toArticle() {
        return Article.from(title, coverUrl, contents);
    }
}
