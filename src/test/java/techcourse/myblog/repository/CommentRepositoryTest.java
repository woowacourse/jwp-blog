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
    private Comment comment;

    @BeforeEach
    void setUp() {
        User user = new User("권민철", "test@test.com", "12345678");
        persistUser = testEntityManager.persist(user);

        Article article = new Article("제목", "coverUrl", "내용");
        article.setAuthor(persistUser);
        persistArticle = testEntityManager.persist(article);
    }

    @Test
    public void Comment_생성시_User_와_Article_매핑_확인_모두_일치() {
        addDefaultComment();
        commentRepository.findById(comment.getId()).ifPresent(savedComment -> {
            assertEquals(savedComment.getUser(), persistUser);
            assertEquals(savedComment.getArticle(), persistArticle);
        });
    }

    private void addDefaultComment() {
        comment = new Comment("댓글내용");
        comment.setUser(persistUser);
        comment.setArticle(persistArticle);
        testEntityManager.persist(comment);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    public void Comment_생성시_User_와_Article_매핑_확인_유저_불일치() {
        addDefaultComment();

        User unrelatedUser = new User("루피", "luffy@luffy.com", "12345678");

        commentRepository.findById(comment.getId()).ifPresent(savedComment -> {
            assertNotEquals(savedComment.getUser(), unrelatedUser);
            assertEquals(savedComment.getArticle(), persistArticle);
        });
    }

    @Test
    public void Comment_생성시_User_와_Article_매핑_확인_아티클_불일치() {
        addDefaultComment();

        Article unrelatedArticle = new Article("상관없는아티클제목", "coverUrl", "내용");

        commentRepository.findById(comment.getId()).ifPresent(savedComment -> {
            assertEquals(savedComment.getUser(), persistUser);
            assertNotEquals(savedComment.getArticle(), unrelatedArticle);
        });
    }

    @Test
    public void articleId로_모든_코멘트_찾기() {
        IntStream.rangeClosed(1, 8)
                .forEach(i -> addCommentAt(persistArticle, i));

        List<Comment> commentList = commentRepository.findAllByArticleId(persistArticle.getId());
        assertThat(commentList.size()).isEqualTo(8);
    }

    private void addCommentAt(Article article, int i) {
        Comment comment = new Comment("댓글내용++추가등록" + i + "번째");
        comment.setUser(persistUser);
        comment.setArticle(article);
        testEntityManager.persist(comment);
    }

     @AfterEach
     void tearDown() {
         commentRepository.deleteAll();
     }
}