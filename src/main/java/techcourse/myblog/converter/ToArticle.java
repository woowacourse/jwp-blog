package techcourse.myblog.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleRequestDto;

@Component
public class ToArticle implements Converter<ArticleRequestDto, Article> {

    private static final String EMPTY_ARTICLE_REQUEST_DTO = "'ArticleRequestDto'가 없습니다.";

    @Override
    public Article convert(ArticleRequestDto source) {
        if (source == null) {
            throw new IllegalArgumentException(EMPTY_ARTICLE_REQUEST_DTO);
        }
        return new Article(source.getTitle(), source.getContents(), source.getCoverUrl());
    }
}
