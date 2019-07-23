package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.exception.UserArticleMissmatchException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService.save(new ArticleDto(1L, 1L, "title", "coverUrl", "contents"));
    }

    @Test
    void Article_userId와_수정하려는_User의_Id가_다르면_수정_실패() {
        ArticleDto articleDto = new ArticleDto(1L, 1L, "title1", "coverUrl1", "contents1");

        assertThatThrownBy(() -> articleService.update(1L, 2L, articleDto))
                .isInstanceOf(UserArticleMissmatchException.class);

    }

    @Test
    void Article_userId와_삭제하려는_User의_Id가_다르면_삭제_실패() {
        ArticleDto articleDto = new ArticleDto(1L, 1L, "title1", "coverUrl1", "contents1");

        assertThatThrownBy(() -> articleService.delete(1L, 2L))
                .isInstanceOf(UserArticleMissmatchException.class);

    }
}
