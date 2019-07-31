package techcourse.myblog.service.article;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.comment.CommentAssembler;
import techcourse.myblog.service.dto.article.ArticleRequestDto;
import techcourse.myblog.service.dto.article.ArticleResponseDto;
import techcourse.myblog.service.dto.comment.CommentResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ArticleAssembler {
    public static ArticleResponseDto convertToDto(final Article article) {
        Objects.requireNonNull(article);

        Long id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();
        if (Objects.isNull(article.getComments())) {
            return new ArticleResponseDto(id, title, coverUrl, contents, Collections.emptyList());
        }
        List<CommentResponseDto> comments = article.getComments().stream()
                .map(CommentAssembler::convertToDto)
                .collect(toList());

        return new ArticleResponseDto(id, title, coverUrl, contents, comments);
    }

    public static Article convertToEntity(final ArticleRequestDto articleDto, final User user) {
        Objects.requireNonNull(articleDto);

        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents, Objects.requireNonNull(user));
    }
}
