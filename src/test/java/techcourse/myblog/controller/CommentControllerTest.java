package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Iterator;

import techcourse.myblog.controller.test.WebClientGenerator;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@ExtendWith(SpringExtension.class)
class CommentControllerTest extends WebClientGenerator {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("루피", "pirates@luff.com", "12345678");
        responseSpec(POST, "/users", parser(userDto));
    }

    @Test
    public void 댓글_추가() {
        generateArticle();
        Article savedArticle = getSavedArticle();

        CommentDto commentDto = new CommentDto("댓글입니다.");
        logInResponseSpec(POST, "/articles/" + savedArticle.getId() + "/comments", userDto, parser(commentDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertTrueResponseArticle(response, commentDto.toDomain()));
    }

    private void generateArticle() {
        ArticleDto articleDto = new ArticleDto("title", "url", "contents");
        logInResponseSpec(POST, "/articles/write", userDto, parser(articleDto));
    }

    private Article getSavedArticle() {
        Iterator<Article> articles = articleRepository.findAll().iterator();
        return articles.next();
    }

    private void assertTrueResponseArticle(EntityExchangeResult<byte[]> response, Comment comment) {
        String createdArticle = responseBody(responseSpec(GET, getRedirectedUri(response)));
        assertThat(createdArticle.contains(comment.getContents())).isTrue();
    }

    @Test
    public void 댓글_삭제() {
        generateArticle();
        Article savedArticle = getSavedArticle();
        generateComment(savedArticle);

        logInResponseSpec(DELETE, "/articles/" + savedArticle.getId() + "/comments/1", userDto)
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    String createdArticle = responseBody(responseSpec(GET, getRedirectedUri(response)));
                    assertThat(createdArticle.contains("댓글입니다.")).isFalse();
                });
    }

    private void generateComment(Article savedArticle) {
        CommentDto commentDto = new CommentDto("댓글입니다.");
        logInResponseSpec(POST, "/articles/" + savedArticle.getId() + "/comments", userDto, parser(commentDto));
    }

    @Test
    public void 댓글_수정() {
        generateArticle();
        Article savedArticle = getSavedArticle();
        generateComment(savedArticle);

        CommentDto editCommentDto = new CommentDto("수정된 댓글입니다.");
        logInResponseSpec(PUT, "/articles/" + savedArticle.getId() + "/comments/1", userDto, parser(editCommentDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    String createdArticle = responseBody(responseSpec(GET, getRedirectedUri(response)));
                    assertThat(createdArticle.contains("수정된 댓글입니다.")).isTrue();
                });
    }

    @AfterEach
    public void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }
}