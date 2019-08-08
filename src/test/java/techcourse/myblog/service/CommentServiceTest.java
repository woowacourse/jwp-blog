package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.dto.UserDto;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.domain.dto.CommentDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.domain.comment.CommentTest.comment;
import static techcourse.myblog.domain.user.UserTest.user;
import static techcourse.myblog.domain.user.UserTest.user2;

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

        assertDoesNotThrow(() -> commentService.createComment(commentDto, LoginUser.toLoginUser(user)));
    }

    @Test
    void 없는_댓글_삭제() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.deleteComment(100L, LoginUser.toLoginUser(user)));
    }

    @Test
    void 타인_댓글_삭제() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.deleteComment(1L,  LoginUser.toLoginUser(user2)));
    }

    @Test
    void 없는_댓글_수정() {
        given(commentRepository.findById(100L)).willReturn(Optional.empty());

        assertThrows(NotFoundObjectException.class, () -> commentService.updateComment(100L, LoginUser.toLoginUser(user), commentDto2));
    }

    @Test
    void 타인_댓글_수정() {
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(InvalidAuthorException.class, () -> commentService.updateComment(1L,  LoginUser.toLoginUser(user2), commentDto2));
    }
}