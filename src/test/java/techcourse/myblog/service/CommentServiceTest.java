package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.CommentRepository;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.user.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class CommentServiceTest {

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    private User user1 = new User("buddy", "buddy@buddy.com", "Aa12345!");
    private User user2 = new User("buddy2", "buddy2@buddy.com", "Aa12345!");
    private CommentDto commentDto = new CommentDto();
    private CommentDto commentDto2 = new CommentDto();
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentDto.setContents("내용입니다.");
        comment = commentDto.toEntity(user1);
        commentDto2.setContents("수정된 내용입니다.");
    }

    @Test
    void 댓글_작성() {
        given(commentRepository.save(comment)).willReturn(comment);

        assertDoesNotThrow(() -> commentService.createComment(commentDto, user1));
    }

    @Test
    void 없는_댓글_삭제() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.deleteComment(100L, user1));
    }

    @Test
    void 타인_댓글_삭제() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.deleteComment(1L, user2));
    }

    @Test
    void 없는_댓글_수정() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.updateComment(100L, user1, commentDto2));
    }

    @Test
    void 타인_댓글_수정() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.updateComment(1L, user2, commentDto2));
    }
}