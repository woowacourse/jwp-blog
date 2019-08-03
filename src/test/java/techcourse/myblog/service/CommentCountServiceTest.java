package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.Utils.TestConstants.BASE_USER_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentCountServiceTest {
    @Autowired
    CommentCountService commentCountService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    private Long articleId;

    @Test
    void 댓글_개수_확인() {
        ArticleDto articleDto = articleService.save(
                BASE_USER_ID, new ArticleDto(null, BASE_USER_ID, "title", "coverUrl", "contents"));
        articleId = articleDto.getId();

        commentService.save(BASE_USER_ID, new CommentRequestDto(articleId, "TEST Comment"));
        commentService.save(BASE_USER_ID, new CommentRequestDto(articleId, "TEST Comment"));
        commentService.save(BASE_USER_ID, new CommentRequestDto(articleId, "TEST Comment"));

        assertThat(commentCountService.countByArticleId(articleId)).isEqualTo(3);
    }
}
