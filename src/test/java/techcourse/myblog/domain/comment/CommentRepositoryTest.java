package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void save() {
        UserDto userDto = UserDto.builder().name("test")
                .email("test@test.com").password("testtset123").build();

        User persistUser = testEntityManager.persist(userDto.toEntity());
        testEntityManager.flush();
        testEntityManager.clear();

        ArticleDto articleDto = ArticleDto.builder().userDto(UserDto.from(persistUser)).build();
        Article article = articleDto.toEntity();

        Article persistArticle = testEntityManager.persist(article);
        testEntityManager.flush();
        testEntityManager.clear();

        Comment comment = Comment.builder().author(persistUser).article(persistArticle).build();
        Comment persistComment = testEntityManager.persist(comment);
        testEntityManager.flush();
        testEntityManager.clear();

        Comment repoComment = commentRepository.findById(persistComment.getId()).get();

        assertThat(repoComment).isEqualTo(comment);
    }
}