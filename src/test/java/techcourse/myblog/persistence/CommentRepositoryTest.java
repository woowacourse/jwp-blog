package techcourse.myblog.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

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
        user = userRepository.save(new User("moomin", "qweQWE123!@#", "moomin@naver.com"));
        article = articleRepository.save(new Article("moomin's article", "moomin is happy", "moomin's background image"));
    }

    @Test
    void 댓글_생성시간_작동_테스트() {
        Comment comment = Comment.builder()
                .comment("myComment")
                .commenter(user)
                .article(article)
                .build();
        commentRepository.save(comment);

        Comment justCreatedComment = commentRepository.findAllByArticleOrderByCreatedAt(article).get(0);
        assertThat(justCreatedComment.getCreatedAt()).isNotNull();
    }
}
