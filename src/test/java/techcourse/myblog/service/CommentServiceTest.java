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
        Comment createdComment = commentService.create(1L, user, comment);

        assertThat(createdComment.getUser()).isEqualTo(user);
        assertThat(createdComment.getArticle()).isEqualTo(article);
    }

    @Test
    void 댓글_수정_테스트() {
        User user = new User();
        Article article = new Article();
        Comment comment = new Comment();
        comment.initialize(user, article);

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        commentService.update("hello", 1L, user);

        assertThat(comment.getContent()).isEqualTo("hello");
    }

    @Test
    void 유저_정보가_일치하지_않을때_댓글_수정_예외_테스트() {
        User user = User.builder()
                .id(1L)
                .build();
        Article article = new Article();
        Comment comment = new Comment();
        comment.initialize(user, article);

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        assertThrows(NotMatchAuthenticationException.class, () -> commentService.update("hello", 1L, new User()));
    }
}