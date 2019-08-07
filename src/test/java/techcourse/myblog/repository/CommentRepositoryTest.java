package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private User user;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = userRepository.findByEmailEmail("test@test.com").get();
        article = articleRepository.findById(1L).get();
        comment = commentRepository.findById(1L).get();
    }

    @Test
    void 주인_댓글_테스트() {
        comment.updateContents(new Comment("edited", article, user), user);
        assertThat(
                articleRepository.findById(article.getId()).get().getComments().stream()
                        .anyMatch(comment -> comment.getContents().equals("edited"))
        ).isTrue();
    }

    @Test
    void 노예_아티클_테스트() {
        article.addComment(new Comment("z", article, user));
        assertThat(articleRepository.findById(article.getId()).get()
                .getComments().stream()
                .anyMatch(comment -> comment.getContents().equals("z"))).isTrue();
    }

    @Test
    void 노예_유저_테스트() {
        user.updateNameAndEmail("update", "test@test.com");
        assertThat(userRepository.findById(user.getId()).get().getName()).isEqualTo("update");
        assertThat(articleRepository.findById(article.getId()).get().getAuthor().getName()).isEqualTo("update");
        assertThat(commentRepository.findById(comment.getId()).get().getAuthor().getName()).isEqualTo("update");
        user.updateNameAndEmail("test", "test@test.com");
    }
}