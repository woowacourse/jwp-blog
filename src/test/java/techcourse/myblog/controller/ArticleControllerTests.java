package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import techcourse.myblog.controller.template.ControllerTestTemplate;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDto;

public class ArticleControllerTests extends ControllerTestTemplate {
    @Autowired
    private ArticleRepository articleRepository;
    private Article article;

    @BeforeEach
    void 게시글_작성_확인() {
        article = new ArticleDto("1", "https://www.naver.com", "2").toArticle();
        articleRepository.save(article);
        checkStatusAndHeaderLocation(response(HttpMethod.POST, "/articles"), HttpStatus.FOUND, ".*articles.*");
    }

    @Test
    void index_페이지_조회() {
        checkStatus(HttpMethod.GET, "/", HttpStatus.OK);
    }

    @Test
    void 게시글_작성_페이지_확인() {
        checkStatus(HttpMethod.GET, "/writing", HttpStatus.OK);
    }

    @Test
    void 게시글_조회() {
        checkStatus(HttpMethod.GET, "/articles/" + article.getId(), HttpStatus.OK);
    }

    @Test
    void 게시글_수정_페이지_확인() {
        checkStatus(HttpMethod.GET, "/articles/" + article.getId() + "/edit", HttpStatus.OK);
    }

    @Test
    void 게시글_수정_확인() {
        checkStatusAndHeaderLocation(response(HttpMethod.PUT, "/articles/" + article.getId()),
                HttpStatus.FOUND,".*articles.*");
    }

    @AfterEach
    void 게시글_삭제_확인() {
        checkStatusAndHeaderLocation(response(HttpMethod.DELETE, "/articles/" + article.getId())
                , HttpStatus.FOUND, ".*/");
    }
}