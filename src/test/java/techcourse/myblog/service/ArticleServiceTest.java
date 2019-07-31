package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.dto.ArticleDto;
import techcourse.myblog.web.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ArticleServiceTest {
    private final ArticleService articleService;
    private final UserService userService;
    private User author;
    private Article beforeArticle;

    @Autowired
    public ArticleServiceTest(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        author = userService.save(new UserDto("aiden", "aiden1@naver.com", "aiden3"));
        beforeArticle = articleService.save(
                new ArticleDto("Aiden jo",
                        "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                        "abc"),
                author
        );
    }

    @Test
    void 조회() {
        assertThat(articleService.findById(beforeArticle.getId())).isEqualTo(beforeArticle);
    }

    @Test
    void 저장_조회_테스트() {
        Article newArticle = articleService.save(
                new ArticleDto("Aiden jo",
                        "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                        "abc"),
                author
        );
        assertThat(articleService.findById(newArticle.getId())).isEqualTo(newArticle);
    }

    @Test
    void 삭제_후_조회_테스트() {
        articleService.delete(beforeArticle.getId());
        assertThatThrownBy(() -> articleService.findById(beforeArticle.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 수정_테스트() {
        ArticleDto articleDto = new ArticleDto("whale kim",
                "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                "update contents");
        Article updatedArticle = articleService.update(beforeArticle.getId(), articleDto);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleDto.getContents());
    }
}