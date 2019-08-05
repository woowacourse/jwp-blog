package techcourse.myblog.application.assembler;

import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;

public class ArticleAssembler implements Assembler<Article, ArticleDto> {
    private static ArticleAssembler assembler = new ArticleAssembler();

    private UserAssembler userAssembler = UserAssembler.getInstance();

    private ArticleAssembler() {
    }

    public static ArticleAssembler getInstance() {
        return assembler;
    }

    @Override
    public ArticleDto convertEntityToDto(Article entity) {
        return new ArticleDto(entity.getId()
                , entity.getTitle()
                , entity.getCoverUrl()
                , entity.getContents()
                , userAssembler.convertEntityToDto(entity.getAuthor()));
    }
}