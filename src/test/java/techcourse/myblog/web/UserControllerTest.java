package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import techcourse.myblog.repository.UserRepository;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class UserControllerTest extends AuthedWebTestClient {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 인덱스_페이지_GET() {
        get("/users")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_실패_테스트() {
        long count = userRepository.count();
        webTestClient.post().uri("/users")
                .body(params(Arrays.asList("name", "password", "email"), "a", "b", "c"))
                .exchange()
                .expectStatus().isUnauthorized();

        assertThat(userRepository.count()).isEqualTo(count);
    }

    @Test
    void 회원가입_성공_테스트() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params(Arrays.asList("name", "password", "email"), "temp", "A!1bcdefg", "temp@test.com"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+\\/login");
    }

    @Test
    void 회원정보_수정_테스트() {
        put("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params(Arrays.asList("name", "email"), "mobumsaeng", "test@test.com"))
                .exchange().expectStatus().is3xxRedirection();
    }
}