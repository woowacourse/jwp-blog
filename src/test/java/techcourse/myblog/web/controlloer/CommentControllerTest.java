package techcourse.myblog.web.controlloer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.CommentDto;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static techcourse.myblog.domain.user.UserTest.user;
import static techcourse.myblog.domain.user.UserTest.user2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest extends AbstractControllerTest {

    @Test
    void 로그인_전_댓글_생성() {
        webTestClient.post().uri("/articles/1/comment")
                .body(BodyInserters
                        .fromFormData("contents", "댓글입니다."))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_후_댓글_생성() {
        String jSessionId = extractJSessionId(login(user));
        CommentDto commentDto = new CommentDto("댓글입니다.");

        webTestClient.post().uri("/articles/1/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .cookie("JSESSIONID", jSessionId)
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..contents").value(hasItem("댓글입니다."));
    }

    @Test
    void 로그인_후_댓글_수정() {
        CommentDto commentDto = new CommentDto("수정된 댓글입니다.");

        String jSessionId = extractJSessionId(login(user));
        webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto),CommentDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..contents").value(hasItem("수정된 댓글입니다."));
    }

    @Test
    void 로그인_후_댓글_삭제() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.delete().uri("/articles/1/comment/2")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$..id").value(not("2"));
    }

    @Test
    void 자신이_쓰지_않은_댓글_수정_시도() {
        String jSessionId = extractJSessionId(login(user2));
        CommentDto commentDto = new CommentDto("수정된 댓글입니다.");

        webTestClient.put().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(commentDto),CommentDto.class)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

    @Test
    void 자신이_쓰지_않은_댓글_삭제_시도() {
        String jSessionId = extractJSessionId(login(user2));
        webTestClient.delete().uri("/articles/1/comment/1")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/");
    }

}
