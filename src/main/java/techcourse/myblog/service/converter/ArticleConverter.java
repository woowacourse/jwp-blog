package techcourse.myblog.service.converter;

import techcourse.myblog.domain.Article;
import techcourse.myblog.service.dto.ArticleDto;

public class ArticleConverter extends Converter<ArticleDto, Article> {
    private static class ArticleConverterLazyHolder {
        private static final ArticleConverter INSTANCE = new ArticleConverter();
    }

    public static ArticleConverter getInstance() {
        return ArticleConverterLazyHolder.INSTANCE;
    }

    private ArticleConverter(){
        super(articleDto -> new Article(articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents())
        , article -> new ArticleDto(article.getTitle(),
                        article.getCoverUrl(),
                        article.getContents()));
    }
}
