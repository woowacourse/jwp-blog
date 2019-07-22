package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
public class UserControllerTest extends LoginTemplate {

    @BeforeEach
    void setUp() {
        registeredWebTestClient();
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "andole@gmail.com"))
                .exchange();
    }

    @Test
    void getIndexTest() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void 회원가입_실패_테스트() {
        long count = userRepository.count();

        webTestClient.post().uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "a")
                        .with("password", "b")
                        .with("email", "c"))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(userRepository.count()).isEqualTo(count);
    }

    @Test
    void 회원가입_성공_테스트() {
        long count = userRepository.count();
        webTestClient.post().uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "test@test.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
        assertThat(userRepository.count()).isNotEqualTo(count);
    }

    @Test
    void 로그인_성공_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 이메일_없음_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "xxx@gmail.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("등록된 이메일이 없습니다.")).isTrue();
                });
    }

    @Test
    void 비밀번호_틀림_테스트() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "B!1bcdefg"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
                });
    }

    @Test
    void 회원정보_수정_테스트() {
        loggedInPutRequest("/users")
                .body(BodyInserters.fromFormData("name", "mobumsaeng")
                        .with("email", "edit@gmail.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();

        assertDoesNotThrow(() -> userRepository.findByEmail("edit@gmail.com").orElseThrow(IllegalAccessError::new));
    }

    @Test
    void 회원정보_삭제_테스트() {
        loggedInDeleteRequest("/users")
                .exchange().
                expectStatus()
                .is3xxRedirection();

        assertThatThrownBy(() -> userRepository.findByEmail("andole@gmail.com").orElseThrow(IllegalAccessError::new))
                .isInstanceOf(IllegalAccessError.class);
    }
}