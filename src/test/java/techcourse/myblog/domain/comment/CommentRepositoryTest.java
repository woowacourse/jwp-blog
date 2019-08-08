package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void checkLocalDate() {
        LocalDateTime localDateTime = LocalDateTime.now();

        User author = userRepository.findById(1L).get();
        Article article = articleRepository.findById(1L).get();
        Comment comment = new Comment("COMMENT", author, article);

        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment.getCreatedDateTime().isAfter(localDateTime));
        assertThat(savedComment.getLastModifiedDateTime().isAfter(localDateTime));
    }
}