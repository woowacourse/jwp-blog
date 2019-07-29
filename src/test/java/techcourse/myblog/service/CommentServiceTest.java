package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    private Long articleId;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = articleService.save(1L,
                new ArticleDto(null, 1L, "title", "url", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    void saveComment() {
        CommentDto commentDto = new CommentDto("TEST");
        Comment comment = commentService.save(1L, articleId, commentDto);

        assertThat(comment.getComment()).isEqualTo(commentDto.getComment());
    }
}