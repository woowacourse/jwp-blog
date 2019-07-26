package techcourse.myblog.service.converter;

import techcourse.myblog.domain.Article;
import techcourse.myblog.service.dto.ArticleRequest;

public class ArticleConverter extends Converter<ArticleRequest, Article> {
    private static class ArticleConverterLazyHolder {
        private static final ArticleConverter INSTNACE = new ArticleConverter();
    }

    public static ArticleConverter getInstance() {
        return ArticleConverterLazyHolder.INSTNACE;
    }

    private ArticleConverter(){
        super(articleRequest -> new Article(articleRequest.getTitle(),
                articleRequest.getCoverUrl(),
                articleRequest.getContents())
        , article -> new ArticleRequest(article.getTitle(),
                        article.getCoverUrl(),
                        article.getContents()));
    }
}
