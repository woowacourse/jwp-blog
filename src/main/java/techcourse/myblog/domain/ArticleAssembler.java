package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleDto;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleAssembler {
    public static ArticleDto writeDto(Article article) {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setArticleId(article.getArticleId());
        articleDto.setTitle(article.getTitle());
        articleDto.setCoverUrl(article.getCoverUrl());
        articleDto.setContents(article.getContents());

        return articleDto;
    }

    public static List<ArticleDto> writeDtos(List<Article> articles) {
        return articles.stream()
                .map(ArticleAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static Article writeArticle(ArticleDto articleDto) {
        return new Article(articleDto.getArticleId(), articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
    }
}
