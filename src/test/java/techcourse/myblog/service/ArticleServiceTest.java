package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.domain.user.UserTest;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.domain.dto.ArticleDto;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.domain.article.ArticleTest.article;
import static techcourse.myblog.domain.user.UserTest.user;


@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;

    private ArticleDto articleDto = new ArticleDto();

    @Test
    void 글_생성() {
        given(httpSession.getAttribute("user")).willReturn(user);
        given(articleRepository.save(any(Article.class))).willReturn(new Article());

        assertDoesNotThrow(() -> articleService.createArticle(articleDto, user));
    }

    @Test
    void 글_조회() {
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        assertDoesNotThrow(() -> articleService.findArticleAndGetDto(1L));
    }

    @Test
    void 없는_글_조회() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NotFoundObjectException.class, () -> articleService.findArticleAndGetDto(1L));
    }
}
