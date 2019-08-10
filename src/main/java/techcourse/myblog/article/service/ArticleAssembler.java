package techcourse.myblog.article.service;

import techcourse.myblog.article.Article;
import techcourse.myblog.article.dto.ArticleRequest;
import techcourse.myblog.article.dto.ArticleResponse;
import techcourse.myblog.comment.dto.CommentResponse;
import techcourse.myblog.comment.service.CommentAssembler;
import techcourse.myblog.user.User;

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
