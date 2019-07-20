package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.web.controller.dto.ArticleDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    private Article beforeArticle;

    @BeforeEach
    void setUp() {
        beforeArticle = articleService.save(
                new ArticleDto("Aiden",
                        "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                        "abc")
        );
    }

    @Test
    void 조회() {
        assertThat(articleService.findById(beforeArticle.getId())).isEqualTo(beforeArticle);
    }

    @Test
    void 저장_조회_테스트() {
        Article newArticle = articleService.save(
                new ArticleDto("Aiden",
                        "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                        "abc")
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
        ArticleDto articleDto = new ArticleDto("김고래",
                "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg",
                "다나가");
        Article updatedArticle = articleService.update(beforeArticle.getId(), articleDto);
        assertThat(updatedArticle.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(updatedArticle.getCoverUrl()).isEqualTo(articleDto.getCoverUrl());
        assertThat(updatedArticle.getContents()).isEqualTo(articleDto.getContents());
    }
}