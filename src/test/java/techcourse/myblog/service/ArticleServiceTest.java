package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.ArticleRepository;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private HttpSession httpSession;

    private User user = new User();
    private ArticleDto articleDto = new ArticleDto();
    private Article article = new Article();

    @Test
    void 글_생성() {
        given(httpSession.getAttribute("user")).willReturn(user);
        given(articleRepository.save(article)).willReturn(article);

        assertDoesNotThrow(() -> articleService.createArticle(articleDto, httpSession));
    }

    @Test
    void 글_생성_예외() {
        given(httpSession.getAttribute("user")).willReturn(null);

        assertThrows(NotFoundObjectException.class, () -> articleService.createArticle(articleDto, httpSession));
    }

    @Test
    void 글_조회() {
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        assertDoesNotThrow(() -> articleService.findArticle(1L));
    }

    @Test
    void 없는_글_조회() {
        given(articleRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(NotFoundObjectException.class, () -> articleService.findArticle(1L));
    }
}
