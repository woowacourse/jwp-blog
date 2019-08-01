package techcourse.myblog.article.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.repository.ArticleRepository;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {
    private static final String TITLE = "게시글 제목";
    private static final String CONTENTS = "게시글 내용";
    private static final String COVER_URL = "게시글 배경";

    private static final String NAME = "아잌아잌";
    private static final String PASSWORD = "@Password1234";
    private static final String EMAIL = "ike@ike.com";
    private static final User AUTHOR = new User(NAME, PASSWORD, EMAIL);

    @Autowired
    private ArticleService articleService;

    @MockBean(name = "articleRepository")
    private ArticleRepository articleRepository;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(TITLE, CONTENTS, COVER_URL, AUTHOR);
    }

    @Test
    public void 게시글_작성자와_게시글_수정_또는_삭제하려는_사용자가_일치하는_경우() {
        // given
        UserResponseDto userResponseDto = new UserResponseDto(NAME, EMAIL);
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        // then
        assertDoesNotThrow(() -> articleService.checkAuthentication(article.getId(), userResponseDto));
    }

    @Test
    public void 작성자_외의_사용자가_게시글을_수정_또는_삭제하려고_하는_경우_예외처리() {
        // given
        UserResponseDto userResponseDto = new UserResponseDto("에헴에헴", "ehem@ehem.com");
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        // then
        assertThrows(NotFoundArticleException.class, () -> articleService.checkAuthentication(article.getId(), userResponseDto));
    }
}