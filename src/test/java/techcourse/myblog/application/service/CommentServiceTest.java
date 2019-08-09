package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private ArticleService articleService;

    @Mock
    private CommentRepository CommentRepository;


    private InOrder inOrder;
    Comment comment;
    CommentResponseDto commentResponseDto;
    CommentRequestDto commentRequestDto;
    User user;
    Article article;

    @BeforeEach
    void setUp() {
        inOrder = inOrder(CommentRepository);
        String contents = "aaaaaa";
        user = new User("zino@zino.zino", "hyo.hyo.hyo", "zhiynooh");
        article = new Article("title", "coverUrl", "반갑다 나는 효오다", user);

        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents(contents);
        comment = new Comment(contents, user, article);
        commentResponseDto = CommentResponseDto.of(comment);
    }

    @Test
    void 저장_테스트() {
        long articleId = 1;
        given(CommentRepository.save(comment)).willReturn(comment);
        given(userService.findUserByEmail(user.getEmail())).willReturn(user);
        given(articleService.findArticleById(articleId)).willReturn(article);

        commentService.save(commentRequestDto, user.getEmail(), articleId);

        verify(CommentRepository, times(1)).save(comment);
    }

    @Test
    void 업데이트_테스트() {
        long commentId = 1;
        given(CommentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(userService.findUserByEmail(user.getEmail())).willReturn(user);

        assertDoesNotThrow(() -> commentService.update(commentId, commentRequestDto, user.getEmail()));
    }

    @Test
    void 삭제_테스트() {
        long commentId = 1;
        given(CommentRepository.findById(commentId)).willReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.delete(commentId, user.getEmail()));
    }
}