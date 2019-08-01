package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;

public class ArticleConverter implements Converter<ArticleDto, ArticleDto, Article> {
    private static ArticleConverter converter = new ArticleConverter();

    public static ArticleConverter getInstance() {
        return converter;
    }

    @Override
    public Article convertFromDto(ArticleDto dto) {
        return new Article.ArticleBuilder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .coverUrl(dto.getCoverUrl())
                .build();
    }

    @Override
    public ArticleDto convertFromEntity(Article entity) {
        return new ArticleDto(entity.getId()
                , entity.getTitle()
                , entity.getCoverUrl()
                , entity.getContents());
    }
}
