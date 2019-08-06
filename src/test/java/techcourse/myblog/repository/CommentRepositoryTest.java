package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.IntStream;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User persistUser;
    private Article persistArticle;
    private Comment persistComment;

    @BeforeEach
    void setUp() {
        User user = new User("권민철", "test@test.com", "12345678");
        persistUser = testEntityManager.persist(user);

        Article article = new Article("제목", "coverUrl", "내용", persistUser);

        persistArticle = testEntityManager.persist(article);

        Comment comment = new Comment("댓글내용", persistUser, persistArticle);

        persistComment = testEntityManager.persist(comment);
        persistArticle.add(persistComment);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    public void Comment_생성시_User_매핑_일치_확인() {
        Comment savedComment = commentRepository
                .findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않음"));

        assertEquals(savedComment.getAuthor(), persistUser);
    }

    @Test
    public void Comment_생성시_User_와_Article_매핑_확인_유저_불일치() {
        User unrelatedUser = new User("루피", "luffy@luffy.com", "12345678");

        Comment savedComment = commentRepository
                .findById(persistComment.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("없음"));

        assertNotEquals(savedComment.getAuthor(), unrelatedUser);
    }

    @Test
    public void Comment_생성시_User_와_Article_매핑_확인_아티클_불일치() {
        Article unrelatedArticle = new Article("상관없는아티클제목", "coverUrl", "내용");

        commentRepository.findById(persistComment.getId()).ifPresent(savedComment -> {
            assertNotEquals(savedComment.getArticle(), unrelatedArticle);
        });
    }

    @Test
    public void articleId로_모든_코멘트_찾기() {
        commentRepository.deleteAll();

        IntStream.rangeClosed(1, 8)
                .forEach(i -> addCommentAt(persistArticle, i));

        List<Comment> commentList = commentRepository.findAllByArticleId(persistArticle.getId());
        assertThat(commentList.size()).isEqualTo(8);
    }

    private void addCommentAt(Article article, int i) {
        Comment comment = new Comment("댓글내용++추가등록" + i + "번째", persistUser, article);
        testEntityManager.persist(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
    }
}