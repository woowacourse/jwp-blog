package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.comment.CommentRepository;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.CommentDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.comment.CommentTest.comment;
import static techcourse.myblog.user.UserTest.user;
import static techcourse.myblog.user.UserTest.user2;

@SpringBootTest
class CommentServiceTest {

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    private CommentDto commentDto = new CommentDto();
    private CommentDto commentDto2 = new CommentDto();

    @Test
    void 댓글_작성() {
        given(commentRepository.save(comment)).willReturn(comment);

        assertDoesNotThrow(() -> commentService.createComment(commentDto, user));
    }

    @Test
    void 없는_댓글_삭제() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.deleteComment(100L, user));
    }

    @Test
    void 타인_댓글_삭제() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.deleteComment(1L, user2));
    }

    @Test
    void 없는_댓글_수정() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.updateComment(100L, user, commentDto2));
    }

    @Test
    void 타인_댓글_수정() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.updateComment(1L, user2, commentDto2));
    }
}