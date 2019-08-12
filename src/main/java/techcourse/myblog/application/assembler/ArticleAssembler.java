package techcourse.myblog.application.assembler;

import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Component
public class ArticleAssembler {

    public Article convertToArticle(ArticleDto articleDto, User author) {
        return new Article(author, articleDto.getTitle(), articleDto.getCoverUrl(),
                articleDto.getContents());
    }

    public ArticleDto convertToDto(Article article) {
        return new ArticleDto(article.getId(), article.getTitle(), article.getCoverUrl(),
                article.getContents());
    }
}