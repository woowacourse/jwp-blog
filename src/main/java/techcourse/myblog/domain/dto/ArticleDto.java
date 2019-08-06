package techcourse.myblog.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
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
}
