package techcourse.myblog.article.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
public class ArticleCreateDto {
    private String title;
    private String coverUrl;
    private String contents;

    public Article toArticle(User author) {
        return Article.builder()
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .author(author)
                .build();
    }
}
