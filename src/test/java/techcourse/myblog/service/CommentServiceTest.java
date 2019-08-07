package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Article article;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmailEmail("test@test.com").get();
        articleRepository.deleteAll();
        article = articleRepository.save(new Article("a", "b", "c", user));
    }

    @Test
    void 코맨트_추가_테스트() {
        Comment comment = commentService.add(article.getId(), user, new CommentDto("asd"));
        assertThat(comment.getAuthor().getName()).isEqualTo("test");
        assertThat(comment.getArticle().getTitle()).isEqualTo("a");
    }

    @Test
    void 코멘트_삭제_테스트() {
        Comment comment = commentService.add(article.getId(), user, new CommentDto("comment"));
        commentService.delete(comment.getId(), user);
        assertThatThrownBy(() -> commentRepository.findById(comment.getId()).orElseThrow(CommentException::new))
                .isInstanceOf(CommentException.class);
    }

    @Test
    void 코멘트_수정_테스트() {
        Comment comment = commentService.add(article.getId(), user, new CommentDto("abc"));
        article.addComment(comment);
        article = articleRepository.findById(article.getId()).get();
        comment = article.getComments().get(0);
        comment.updateContents("updated", user);
        assertThat(commentRepository.findById(comment.getId()).get().getContents()).isEqualTo("updated");
    }

    @Test
    void 다른사람이_수정_테스트() {
        Comment comment = commentService.add(article.getId(), user, new CommentDto("abc"));
        assertThatThrownBy(() ->
                commentService.update(article.getId(),
                        new User("hacker", "A!1bcdefg", "hacker@hacker.com"),
                        new CommentDto("edit")))
                .isInstanceOf(CommentException.class);
    }

    @Test
    void 다른사람이_삭제_테스트() {
        Comment comment = commentService.add(article.getId(), user, new CommentDto("abc"));
        assertThatThrownBy(() ->
                commentService.delete(article.getId(),
                        new User("hacker", "A!1bcdefg", "hacker@hacker.com")))
                .isInstanceOf(CommentException.class);
    }
}