package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentAssembler;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class CommentRepositoryTest {
    private static final String AUTHOR_NAME = "ike";
    private static final String AUTHOR_EMAIL = "ike@gmail.com";
    private static final String AUTHOR_PASSWORD = "@Password1234";

    private static final String COMMENTER_NAME = "에헴";
    private static final String COMMENTER_EMAIL = "ehem@ehem.com";
    private static final String COMMENTER_PASSWORD = "@Password1234";

    private static final String TITLE = "달";
    private static final String CONTENTS = "\uDF19";
    private static final String COVER_URL = "";

    private static final String COMMENT = "내가 쓴 글~";

    private Article persistArticle;
    private User persistUser;
    private Comment persistComment;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        User commenter = new User(COMMENTER_NAME, COMMENTER_PASSWORD, COMMENTER_EMAIL);
        commenter = testEntityManager.persist(commenter);

        User author = new User(AUTHOR_NAME, AUTHOR_PASSWORD, AUTHOR_EMAIL);
        persistUser = testEntityManager.persist(author);

        Article article = new Article(TITLE, CONTENTS, COVER_URL, author);
        persistArticle = testEntityManager.persist(article);

        Comment comment = new Comment(COMMENT, commenter, article);
        persistComment = testEntityManager.persist(comment);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 댓글_생성() {
        commentRepository.save(persistComment);
        assertThat(commentRepository.findById(persistComment.getId()).get()).isEqualTo(persistComment);
    }

    @Test
    void 특정_댓글_조회_id() {
        assertThat(commentRepository.findById(persistComment.getId()).get().getContents()).isEqualTo(COMMENT);
        assertThatThrownBy(() -> commentRepository.findById(100L).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 특정_게시글의_모든_댓글_찾기() {
        assertThat(commentRepository.findByArticle(persistArticle)).isEqualTo(Collections.singletonList(persistComment));
    }

    @Test
    void 댓글_수정() {
        CommentRequest commentRequestDto = new CommentRequest("haha");
        Comment comment = commentRepository.findById(persistComment.getId()).get();
        comment.update(CommentAssembler.toEntity(commentRequestDto));
        assertThat(commentRepository.findById(persistComment.getId()).get().getContents()).isEqualTo("haha");
    }

    @Test
    void 댓글_삭제() {
        commentRepository.deleteById(persistComment.getId());
        assertThatThrownBy(() -> commentRepository.findById(persistArticle.getId()).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}