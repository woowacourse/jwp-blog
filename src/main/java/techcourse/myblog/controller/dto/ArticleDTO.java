package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import techcourse.myblog.model.Article;

@AllArgsConstructor
@Getter
public class ArticleDTO {
    @NonNull
    private String title;

    @NonNull
    private String contents;

    @NonNull
    private String coverUrl;

    public Article toDomain() {
        return new Article(title, contents, coverUrl);
    }
}
