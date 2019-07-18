package techcourse.myblog.service;

import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

public class ArticleAssembler {
    public ArticleDto convertToDto(final Article article) {
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();
        return new ArticleDto(title, coverUrl, contents);
    }

    public Article convertToEntity(final ArticleDto articleDto) {
        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents);
    }
}
