package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.template.LoginTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends LoginTemplate {

    @BeforeEach
    void setUp() {
        registeredWebTestClient();
    }

    @Test
    void getIndexTest() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 회원가입_실패시_예외처리() {
        webTestClient.post().uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "a")
                        .with("password", "b")
                        .with("email", "c"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_성공_테스트() {
        String newEmail = "test@test.com";
        webTestClient.post().uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", NAME)
                        .with("password", PASSWORD)
                        .with("email", newEmail))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=.*");
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto(null, PASSWORD, EMAIL);

        // when
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/;jsessionid=.*");
    }

    @Test
    void 이메일_없음_테스트() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto(null, PASSWORD, "xxx@gmail.com");

        // when
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().consumeWith(res -> {
            String s = new String(res.getResponseBody());

            // then
            assertThat(s).isEqualTo("등록된 이메일이 없습니다.");
        });
    }

    @Test
    void 비밀번호_틀림_테스트() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto(null, "B!1bcdefg", EMAIL);

        // when
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().consumeWith(res -> {
            String s = new String(res.getResponseBody());

            // then
            assertThat(s).isEqualTo("비밀번호가 일치하지 않습니다.");
        });
    }

    @Test
    void 회원정보_수정_테스트() {
        loggedInPutRequest("/users")
                .body(BodyInserters.fromFormData("name", "mobumsaeng")
                        .with("email", EMAIL))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/mypage.*");
    }

    @Test
    void 회원정보_삭제_테스트() {
        loggedInDeleteRequest("/users")
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/.*");
    }
}