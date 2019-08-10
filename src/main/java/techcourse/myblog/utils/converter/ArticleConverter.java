package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.ArticleRequestDto;

public class ArticleConverter {
    public static Article toEntity(ArticleRequestDto articleRequestDto, User author) {
        String title = articleRequestDto.getTitle();
        String contents = articleRequestDto.getContents();
        String coverUrl = articleRequestDto.getCoverUrl();

        return new Article(title, contents, coverUrl, author);
    }
}
