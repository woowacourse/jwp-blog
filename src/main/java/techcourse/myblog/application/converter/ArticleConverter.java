package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;

public class ArticleConverter extends Converter<ArticleDto, Article> {

    private static class ArticleConverterHolder {
        private static final ArticleConverter INSTANCE = new ArticleConverter();
    }

    public static ArticleConverter getInstance() {
        return ArticleConverter.ArticleConverterHolder.INSTANCE;
    }

    private ArticleConverter() {
        super(articleDto -> new Article(
                        articleDto.getTitle(),
                        articleDto.getCoverUrl(),
                        articleDto.getContents()
                ),
                article -> new ArticleDto(
                        article.getId(),
                        article.getTitle(),
                        article.getCoverUrl(),
                        article.getContents())
        );
    }
}
