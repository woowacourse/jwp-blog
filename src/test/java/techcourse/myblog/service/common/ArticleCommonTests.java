package techcourse.myblog.service.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.service.dto.ArticleDto;

@SpringBootTest
public class ArticleCommonTests {
    @Autowired
    protected ArticleWriteService articleWriteService;
    @Autowired
    protected ArticleReadService articleReadService;
    protected ArticleDto articleDto;
    protected Article article;

    @BeforeEach
    void 글_작성() {
        articleDto = new ArticleDto("title", "coverUrl", "content");
        article = articleWriteService.save(articleDto);
    }

    @AfterEach
    void 글_삭제() {
        articleWriteService.deleteById(article.getId());
    }
}
