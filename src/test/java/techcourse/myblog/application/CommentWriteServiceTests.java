package techcourse.myblog.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.utils.ArticleTestObjects.ARTICLE_FEATURE;
import static techcourse.myblog.utils.CommentTestObjects.COMMENT_DTO;
import static techcourse.myblog.utils.CommentTestObjects.UPDATE_COMMENT_DTO;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

public class CommentWriteServiceTests {
    @Mock
    protected CommentRepository commentRepository;
    private CommentReadService commentReadService;
    private CommentWriteService commentWriteService;
    private User reader;
    private Article article;
    private Long id = 2L;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        commentReadService = new CommentReadService(commentRepository);
        commentWriteService = new CommentWriteService(commentRepository, commentReadService);
        reader = READER_DTO.toUser();
        article = ARTICLE_FEATURE.toArticle(reader);
    }

    @Test
    void 댓글_수정() {
        Comment comment = COMMENT_DTO.toComment(reader, article);
        CommentDto updateCommentDto = UPDATE_COMMENT_DTO;

        given(commentRepository.findById(id)).willReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentWriteService.modify(id, updateCommentDto.toComment(reader, article)));
        compareComment(commentRepository.findById(id).get(), updateCommentDto.toComment(reader, article));
    }

    void compareComment(Comment comment1, Comment comment2) {
        assertEquals(comment1.getContents(), comment2.getContents());
        assertEquals(comment1.getArticle(), comment2.getArticle());
        assertEquals(comment1.getWriter(), comment2.getWriter());
    }
}
