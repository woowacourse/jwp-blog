package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User.UserEmail;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserControllerTest extends AuthedWebTestClient {

    @BeforeEach
    void setUp() {
        init();
    }

    @AfterEach
    void tearDown() {
        end();
    }

    @Test
    void 인덱스_페이지_GET() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_실패_테스트() {
        long count = userRepository.count();
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "a")
                        .with("password", "b")
                        .with("email", "c"))
                .exchange()
                .expectStatus().isAccepted();

        assertThat(userRepository.count()).isEqualTo(count);
    }

    @Test
    void 회원가입_성공_테스트() {
        long count = userRepository.count();
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "test@test.com"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+\\/login.+");
        assertThat(userRepository.count()).isNotEqualTo(count);
    }

    @Test
    void 회원정보_수정_테스트() {
        put("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "mobumsaeng")
                        .with("email", "edit@gmail.com"))
                .exchange().expectStatus().is3xxRedirection();

        assertDoesNotThrow(() -> userRepository.findByEmail(UserEmail.of("edit@gmail.com")).orElseThrow(IllegalAccessError::new));
    }

    @Test
    void 회원정보_삭제_테스트() {
        delete("/users")
                .exchange().expectStatus().is3xxRedirection();

        assertThatThrownBy(() -> userRepository.findByEmail(UserEmail.of("andole@gmail.com")).orElseThrow(IllegalAccessError::new))
                .isInstanceOf(IllegalAccessError.class);
    }
}