package techcourse.myblog.service;

import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

public class ArticleAssembler {
    public ArticleDto convertToDto(final Article article) {
        int id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();

        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        articleDto.setId(id);

        return articleDto;
    }

    public Article convertToEntity(final ArticleDto articleDto) {
        int id = articleDto.getId();
        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();

        return new Article(id, title, coverUrl, contents);
    }
}
