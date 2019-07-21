package techcourse.myblog.translator;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

@Component
public class ArticleTranslator implements ModelTranslator<Article, ArticleDto> {
    @Override
    public Article toEntity(final Article article, final ArticleDto articleDto) {
        Long id = processValue(article.getId(), articleDto.getId());
        String title = updateValue(articleDto.getTitle(), article.getTitle());
        String coverUrl = updateValue(articleDto.getCoverUrl(), article.getCoverUrl());
        String contents = updateValue(articleDto.getContents(), article.getContents());

        return Article.builder()
                .id(id)
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .build();
    }

    private <T> T processValue(T domainValue, T dtoValue) {
        return domainValue != null ? domainValue : dtoValue;
    }

    private <T> T updateValue(T value, T updateValue) {
        return updateValue == null ? value : updateValue;
    }
}
