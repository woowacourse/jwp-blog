package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.service.common.ArticleCommonTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleReadServiceTests extends ArticleCommonTests {
    @Test
    void 글_조회_성공() {
        assertTrue(articleReadService.findById(article.getId()).isPresent());
    }
    
    @Test
    void 글_조회_실패() {
        assertFalse(articleReadService.findById(article.getId() + 1).isPresent());
    }
}
