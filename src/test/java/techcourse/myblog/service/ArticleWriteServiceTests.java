package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.common.ArticleCommonTests;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleWriteServiceTests extends ArticleCommonTests {

    @Test
    void 게시글_수정_확인() {
        ArticleDto updateArticleDto = new ArticleDto("new-title", "new-coverUrl", "new-contents");
        articleWriteService.update(article.getId(), updateArticleDto);
        Optional<Article> optionalArticle = articleReadService.findById(article.getId());
        
        assertEquals("new-title", optionalArticle.get().getTitle());
        assertEquals("new-coverUrl", optionalArticle.get().getCoverUrl());
        assertEquals("new-contents", optionalArticle.get().getContents());
    }
}
