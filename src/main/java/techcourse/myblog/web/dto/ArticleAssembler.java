package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.ArticleRequestDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ArticleAssembler {
    public static ArticleDto convertToDto(final Article article) {
        Objects.requireNonNull(article);

        Long id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();
        List<CommentDto> comments = article.getComments().stream()
            .map(CommentAssembler::convertToDto)
            .collect(toList());

        return new ArticleDto(id, title, coverUrl, contents, comments);
    }

    public static Article convertToEntity(final ArticleRequestDto articleDto, final User user) {
        Objects.requireNonNull(articleDto);

        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents, Objects.requireNonNull(user));
    }
}
