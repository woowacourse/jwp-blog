package techcourse.myblog.service;

import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

import java.util.Objects;

public class ArticleAssembler {
    public ArticleDto convertToDto(final Article article) {
        if (Objects.isNull(article)) {
            throw new NullPointerException();
        }
        int id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();

        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        articleDto.setId(id);
        return articleDto;
    }

    public Article convertToEntity(final ArticleDto articleDto) {
        if (Objects.isNull(articleDto)) {
            throw new NullPointerException();
        }
        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents);
    }
}
