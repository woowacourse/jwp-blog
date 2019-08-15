package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.*;
import techcourse.myblog.service.exception.NoCommentException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest()
class CommentServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceTest.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private TestDomainFactory testDomainFactory;

    @MockBean(name = "commentRepository")
    private CommentRepository commentRepository;

    @MockBean(name = "articleService")
    private ArticleService articleService;

    @Test
    void isExist_존재하는_comment() {
        User user = testDomainFactory.newUser();
        Article article = testDomainFactory.newArticleWithoutAuthor();
        article.setAuthor(user);
        Comment comment = testDomainFactory.newCommentWithArticleAndCommenter(article, user);
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));


        assertThat(commentService.isExist(comment.getId())).isTrue();
    }

    @Test
    void isExist_존재하지않는_comment() {
        given(commentRepository.findById(any()))
                .willReturn(Optional.empty());


        Long anyId = 123l;
        assertThat(commentService.isExist(anyId)).isFalse();
    }

    @Test
    void isCommenter_올바른_commenter() {
        User author = testDomainFactory.newUser();
        User commenter = testDomainFactory.newUser();
        Article article = testDomainFactory.newArticleWithoutAuthor();
        article.setAuthor(author);
        Comment comment = testDomainFactory.newCommentWithArticleAndCommenter(article, commenter);
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));


        assertThat(commentService.isCommenter(comment.getId(), commenter)).isTrue();
    }

    @Test
    void isCommenter_commenter가_아닌경우() {
        User author = testDomainFactory.newUser();
        User commenter = testDomainFactory.newUser();
        Article article = testDomainFactory.newArticleWithoutAuthor();
        article.setAuthor(author);
        Comment comment = testDomainFactory.newCommentWithArticleAndCommenter(article, commenter);
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));


        User anyUser = testDomainFactory.newUser();
        assertThat(commentService.isCommenter(comment.getId(), anyUser)).isFalse();
    }

    @Test
    void isCommenter_존재하지않는_comment() {
        given(commentRepository.findById(any()))
                .willReturn(Optional.empty());


        Long anyCommentId = 123l;
        User anyUser = mock(User.class);
        assertThat(commentService.isCommenter(anyCommentId, anyUser)).isFalse();
    }

    @Test
    void findById_존재하지않는_comment() {
        Long commentId = 1L;
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());


        assertThrows(NoCommentException.class, () -> commentService.findById(commentId));
    }

    @Test
    void deleteById() {
        Long commentId = 1L;


        commentService.deleteById(commentId);


        verify(commentRepository).deleteById(commentId);
    }
}