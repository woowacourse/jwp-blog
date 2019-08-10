package techcourse.myblog.application.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;

@Component
public class ArticleConverter extends Converter<ArticleDto, Article> {
    private static Logger logger = LoggerFactory.getLogger(ArticleConverter.class);

    private ArticleConverter() {
        super(articleDto -> new Article(new Article.ArticleBuilder()
                        .title(articleDto.getTitle())
                        .contents(articleDto.getContents())
                        .coverUrl(articleDto.getCoverUrl())
                        .author(articleDto.getAuthor()))
                , article -> {
                    ArticleDto articleDto = new ArticleDto(article.getId()
                            , article.getTitle()
                            , article.getCoverUrl()
                            , article.getContents()); //getAuthor지움
                    logger.info("Article : {}", article);
                    logger.info("ArticleDto Author  : {}", articleDto.getAuthor());

                    return articleDto;
                });
    }
}
