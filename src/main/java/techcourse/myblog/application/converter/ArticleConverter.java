package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;

public class ArticleConverter extends Converter<ArticleDto, Article> {
    private static ArticleConverter converter = new ArticleConverter();

    private ArticleConverter() {
        super(articleDto -> new Article(new Article.ArticleBuilder()
                        .title(articleDto.getTitle())
                        .contents(articleDto.getContents())
                        .coverUrl(articleDto.getCoverUrl()))
                , article -> new ArticleDto(article.getId()
                        , article.getTitle()
                        , article.getCoverUrl()
                        , article.getContents()));
    }

    public static ArticleConverter getInstance() {
        return converter;
    }
}
