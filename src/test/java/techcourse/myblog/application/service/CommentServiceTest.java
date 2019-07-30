package techcourse.myblog.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository CommentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    private InOrder inOrder;

    User user;
    Article article;
    Comment comment;
    CommentDto commentDto;
    String email = "zino@zino.zino";

    @BeforeEach
    void setUp() {
        inOrder = inOrder(CommentRepository, userRepository, articleRepository);

        commentDto = new CommentDto();
        user = new User("zino@zino.zino", "hyo.hyo.hyo", "zhiynooh");
        article = new Article("title", "coverUrl", "반갑다 나는 효오다", user);
        comment = new Comment("빵가워요", user, article);
    }



    @Test
    void 저장_테스트() {
        commentDto.setContents("빵가워요");
        commentDto.setId(comment.getId());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(articleRepository.findById(article.getId())).willReturn(Optional.of(article));
        given(CommentRepository.save(comment)).willReturn(comment);

        assertThat(commentService.save(commentDto, user.getEmail(), article.getId())).isEqualTo(commentDto.getId());
    }
}