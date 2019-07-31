package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class CommentRepositoryTest {

    public static final String CONTENTS_2 = "댓글 내용2";
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User persistAuthor;
    private Article persistArticle;

    @Test
    void 댓글_추가_확인() {
        defaultComment();

        flushAndClearEntityManager();

        List<Comment> actualComment = commentRepository.findAllByArticle(persistArticle);
        assertThat(actualComment.size()).isEqualTo(1);
    }

    private Comment defaultComment() {
        User user = new User("olleh", "test@test.com", "1234");
        persistAuthor = testEntityManager.persist(user);

        Article article = new Article("title", "url", "content", user);
        persistArticle = testEntityManager.persist(article);

        Comment comment = new Comment("댓글 내용", persistArticle, persistAuthor);

        return testEntityManager.persist(comment);
    }

    private void flushAndClearEntityManager() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 서로_다른_아티클의_댓글이_가진_아티클이_다른지_확인() {
        Comment persistComment = defaultComment();

        flushAndClearEntityManager();

        Comment actualComment = commentRepository.findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Dd"));

        assertThat(actualComment.getAuthor()).isEqualTo(persistAuthor);
    }

    @Test
    void 댓글_수정됐는지_확인() {
        Comment persistComment = defaultComment();
        flushAndClearEntityManager();

        Comment actualComment = commentRepository.findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Dd"));

        flushAndClearEntityManager();

        actualComment = commentRepository.findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Dd"));

        assertThat(actualComment.getContents()).isEqualTo(CONTENTS_2);
    }

    @Test
    void 댓글_삭제됐는지_확인() {
        Comment persistComment = defaultComment();
        flushAndClearEntityManager();

        Comment actualComment = commentRepository.findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Dd"));

        commentRepository.delete(actualComment);

        assertThrows(IllegalArgumentException.class, () -> {
            commentRepository.findById(persistComment.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Dd"));
        });
    }

}