package techcourse.myblog.application;

import techcourse.myblog.application.dto.ArticleResponseDto;
import techcourse.myblog.domain.article.Article;

public class ArticleAssembler {
    public static ArticleResponseDto buildArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .title(article.getArticleFeature().getTitle())
                .coverUrl(article.getArticleFeature().getCoverUrl())
                .contents(article.getArticleFeature().getContents())
                .userId(article.getAuthor().getId())
                .email(article.getAuthor().getEmail())
                .name(article.getAuthor().getName())
                .build();
    }
}
