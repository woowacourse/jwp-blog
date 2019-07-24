package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.Article;

@RequiredArgsConstructor
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
