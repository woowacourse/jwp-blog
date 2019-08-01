package techcourse.myblog.translator;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;

public class ArticleTranslator implements ModelTranslator<Article, ArticleDto> {
    @Override
    public Article toEntity(final Article article, final ArticleDto articleDto) {
        Long id = processValue(article.getId(), articleDto.getId());
        String title = updateValue(article.getTitle(), articleDto.getTitle());
        String coverUrl = updateValue(article.getCoverUrl(), articleDto.getCoverUrl());
        String contents = updateValue(article.getContents(), articleDto.getContents());
        User user = updateValue(article.getAuthor(), articleDto.getAuthor());

        return Article.builder()
                .id(id)
                .title(title)
                .coverUrl(coverUrl)
                .contents(contents)
                .author(user)
                .build();
    }

    @Override
    public ArticleDto toDto(Article article, ArticleDto articleDto) {
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setCoverUrl(article.getCoverUrl());
        articleDto.setContents(article.getContents());
        articleDto.setAuthor(article.getAuthor());

        return articleDto;
    }

    private <T> T processValue(T domainValue, T dtoValue) {
        return domainValue != null ? domainValue : dtoValue;
    }

    private <T> T updateValue(T value, T updateValue) {
        return updateValue == null ? value : updateValue;
    }
}
