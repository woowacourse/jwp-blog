package techcourse.myblog.service.article;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.comment.CommentAssembler;
import techcourse.myblog.service.dto.article.ArticleRequest;
import techcourse.myblog.service.dto.article.ArticleResponse;
import techcourse.myblog.service.dto.comment.CommentResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ArticleAssembler {
    public static ArticleResponse convertToDto(final Article article) {
        Objects.requireNonNull(article);

        Long id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();
        if (Objects.isNull(article.getComments())) {
            return new ArticleResponse(id, title, coverUrl, contents, Collections.emptyList());
        }
        List<CommentResponse> comments = article.getComments().stream()
                .map(CommentAssembler::convertToDto)
                .collect(toList());

        return new ArticleResponse(id, title, coverUrl, contents, comments);
    }

    public static Article convertToEntity(final ArticleRequest articleDto, final User user) {
        Objects.requireNonNull(articleDto);

        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents, Objects.requireNonNull(user));
    }
}
