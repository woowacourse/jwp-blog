package techcourse.myblog.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.AuthedWebTestClient;

class ApiUserControllerTest extends AuthedWebTestClient {

    @Test
    void createTest() {
        UserDto userDto = new UserDto("abc", "A!1bcdefg", "abc@test.com");
        webTestClient.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userDto), UserDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("abc");
    }

    @Test
    void updateTest() {
        UserDto userDto = new UserDto("zxc", "A!1bcdefg", "test@test.com");
        putJson("/api/users/1")
                .body(Mono.just(userDto), UserDto.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.name").isEqualTo("zxc");
    }

    @Test
    void readTest() {
        get("/api/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.email").isNotEmpty();
    }
}