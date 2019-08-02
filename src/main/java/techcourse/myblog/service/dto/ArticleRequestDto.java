package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.Article;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {
    private long id;
    private String title;
    private String contents;
    private String coverUrl;

    public ArticleRequestDto(long id, String title, String contents, String coverUrl) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public Article toArticle() {
        return new Article(title, contents, coverUrl);
    }
}