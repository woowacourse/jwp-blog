package techcourse.myblog.service.assembler;

import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.dto.ArticleDto;

public class ArticleAssembler extends Assembler<ArticleDto, Article> {
    private static class ArticleAssemblerLazyHolder {
        private static final ArticleAssembler INSTANCE = new ArticleAssembler();
    }

    public static ArticleAssembler getInstance() {
        return ArticleAssemblerLazyHolder.INSTANCE;
    }

    private ArticleAssembler() {
        super(articleDto -> new Article(
                        articleDto.getTitle(),
                        articleDto.getCoverUrl(),
                        articleDto.getContents())
                , article -> new ArticleDto(
                        article.getId(),
                        article.getTitle(),
                        article.getCoverUrl(),
                        article.getContents()));
    }
}
