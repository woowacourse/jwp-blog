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
        user = userRepository.save(new User("andole", "A!1bcdefg", "andole@gmail.com"));
        article = articleRepository.save(new Article("a", "b", "c", user));
        comment = commentRepository.save(new Comment("a", article, user));
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
        assertThat(articleRepository.findById(article.getId()).get().getComments().get(0).getContents()).isEqualTo("a");
        assertThat(commentRepository.findAll().size()).isEqualTo(2);
        assertThat(commentRepository.findAll().get(0).getContents()).isEqualTo("a");
    }

    @Test
    void 노예_유저_테스트() {
        user.updateNameAndEmail("name", "andole@naver.com");
        assertThat(userRepository.findById(user.getId()).get().getName()).isEqualTo("name");
        assertThat(articleRepository.findById(article.getId()).get().getAuthor().getName()).isEqualTo("name");
        assertThat(commentRepository.findById(comment.getId()).get().getAuthor().getName()).isEqualTo("name");
    }
}