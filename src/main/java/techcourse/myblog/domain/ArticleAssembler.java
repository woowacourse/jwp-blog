package techcourse.myblog.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ArticleAssembler {
    public ArticleDto writeDto(Article article) {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setArticleId(article.getArticleId());
        articleDto.setTitle(article.getTitle());
        articleDto.setCoverUrl(article.getCoverUrl());
        articleDto.setContents(article.getContents());

        return articleDto;
    }

    public List<ArticleDto> writeDtos(Iterable<Article> articles) {
        return StreamSupport.stream(articles.spliterator(), false)
                .map(this::writeDto)
                .collect(Collectors.toList());
    }

    public Article writeArticle(ArticleDto articleDto) {
        return new Article(articleDto.getArticleId(), articleDto.getTitle(), articleDto.getTitle(), articleDto.getContents());
    }
}
