package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.ArticlePageableRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.web.support.UserSessionInfo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticlePageableRepository articlePageableRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 코맨트_추가_테스트() {
        Article article = articlePageableRepository.save(new Article("a", "b", "c"));
        userRepository.save(new User("andole", "aA1231!!", "andole87@gmail.com"));
        UserSessionInfo userSessionInfo = new UserSessionInfo("andole", "andole87@gmail.com");
        Comment comment = commentService.addComment(article.getId(), userSessionInfo.getEmail(), new CommentDto("asd"));

        assertThat(comment.getAuthor().getName()).isEqualTo("andole");
        assertThat(comment.getArticle().getTitle()).isEqualTo("a");
    }
}