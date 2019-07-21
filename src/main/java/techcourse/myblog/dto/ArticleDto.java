package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.Article;

@Getter
@Setter
public class ArticleDto {
    private String title;
    private String contents;
    private String coverUrl;

    public Article toEntity() {
        return new Article(title, contents, coverUrl);
    }
}
