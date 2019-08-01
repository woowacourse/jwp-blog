package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(articleRepository, commentRepository);
    }

    @Test
    void 댓글_생성_테스트() {
        Comment comment = new Comment();
        Article article = new Article();
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        User user = new User();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("van@naver.com");
        userDto.setName("vab");
        userDto.setPassword("!234Qwer");
        Comment createdComment = commentService.create(1L, userDto, comment);

        assertThat(createdComment.getUser().equals("van@naver.com")).isTrue();
        assertThat(createdComment.getArticle()).isEqualTo(article);
    }

    @Test
    void 댓글_수정_테스트() {
        UserDto userDto = new UserDto();
        User user = User.builder()
                .id(1L)
                .name("van")
                .email("van@naver.com")
                .password("!234Qwer")
                .build();

        Article article = new Article();
        Comment comment = new Comment();
        comment.initialize(user, article);

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        userDto.setId(1L);
        userDto.setEmail("van@naver.com");
        userDto.setName("van");
        userDto.setPassword("!234Qwer");
        commentService.update("hello", 1L, userDto);

        assertThat(comment.getContent()).isEqualTo("hello");
    }

    @Test
    void 유저_정보가_일치하지_않을때_댓글_수정_예외_테스트() {
        User user = User.builder()
                .id(1L)
                .name("van")
                .email("van@naver.com")
                .password("!234Qwer")
                .build();
        UserDto userDto = new UserDto();
        Article article = new Article();
        Comment comment = new Comment();
        comment.initialize(user, article);

        userDto.setId(2L);
        userDto.setEmail("aaaaaaa@naver.com");
        userDto.setName("vab");
        userDto.setPassword("!234Qwer");

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(NotMatchAuthenticationException.class, () -> commentService.update("hello", 1L, userDto));
    }
}