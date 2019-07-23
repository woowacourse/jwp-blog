package techcourse.myblog.service.article;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.article.ArticleDto;

import java.util.Objects;

public class ArticleAssembler {
    public static ArticleDto convertToDto(final Article article) {
        if (Objects.isNull(article)) {
            throw new NullPointerException();
        }
        Long id = article.getId();
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();

        ArticleDto articleDto = new ArticleDto(title, coverUrl, contents);
        articleDto.setId(id);
        return articleDto;
    }

    public static Article convertToEntity(final ArticleDto articleDto) {
        if (Objects.isNull(articleDto)) {
            throw new NullPointerException();
        }
        String title = articleDto.getTitle();
        String coverUrl = articleDto.getCoverUrl();
        String contents = articleDto.getContents();
        return new Article(title, coverUrl, contents);
    }
}
