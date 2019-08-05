package techcourse.myblog.dto;

import lombok.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSaveRequestDto {
    private String title;
    private String coverUrl;
    private String contents;

    public Article toArticle() {
        return Article.builder()
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .build();
    }

    public Article toArticle(User author) {
        return Article.builder()
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .author(author)
                .build();
    }
}
