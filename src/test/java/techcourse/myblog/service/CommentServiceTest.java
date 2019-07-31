package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
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
        articleRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.flush();
        article = articleRepository.save(new Article("a", "b", "c"));
        user = userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
    }

    @Test
    void 코맨트_추가_테스트() {
        Comment comment = commentService.addComment(article.getId(), "andole@gmail.com", new CommentDto("asd"));
        assertThat(comment.getAuthor().getName()).isEqualTo("andole");
        assertThat(comment.getArticle().getTitle()).isEqualTo("a");
    }

    @Test
    void 코멘트_삭제_테스트() {
        Comment comment = commentService.addComment(article.getId(), "andole@gmail.com", new CommentDto("comment"));
        commentService.deleteComment(comment.getId(), Email.of("andole@gmail.com"));
        assertThatThrownBy(() -> commentRepository.findById(comment.getId()).orElseThrow(CommentException::new))
                .isInstanceOf(CommentException.class);
    }

    @Test
    @Transactional
    void 코멘트_수정_테스트() {
        Comment comment = commentService.addComment(article.getId(), "andole@gmail.com", new CommentDto("abc"));
        article.addComment(comment);
        article = articleRepository.findById(article.getId()).get();
        comment = article.getComments().get(0);
        comment.updateContent("updated");
        assertThat(commentRepository.findById(comment.getId()).get().getContents()).isEqualTo("updated");
    }
}