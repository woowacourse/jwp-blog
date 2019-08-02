package techcourse.myblog.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ArticleDto {
    private static final int EMPTY_ID = 0;
    private long id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article toArticle(User author) {
        return new Article(title,contents,coverUrl,author);
    }
}
