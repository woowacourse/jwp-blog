package techcourse.myblog.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.UserControllerTest.testEmail;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmail(testEmail).get();
        article = articleRepository.findByAuthor(user).get(0);
    }

    @Test
    void 댓글_생성시간_작동_테스트() {
        Comment comment = Comment.builder()
                .comment("myComment")
                .commenter(user)
                .article(article)
                .build();
        commentRepository.save(comment);

        Comment justCreatedComment = commentRepository.findByArticleOrderByCreatedAt(article).get(0);
        assertThat(justCreatedComment.getCreatedAt()).isNotNull();
    }

    @Test
    void 댓글_시간순_정렬_테스트() {
        List<Comment> comments = commentRepository.findByArticleOrderByCreatedAt(article);
        assertThat(comments.get(0).getComment()).isEqualTo("first");
        assertThat(comments.get(1).getComment()).isEqualTo("second");
        assertThat(comments.get(2).getComment()).isEqualTo("third");
    }
}
