package techcourse.myblog.util;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.user.dto.UserLoginDto;

import static techcourse.myblog.user.UserDataForTest.*;

public class UserUtilForTest {

    public static String loginAndGetCookie(WebTestClient webTestClient) {
        UserLoginDto userLoginDto = new UserLoginDto(USER_EMAIL, USER_PASSWORD);

        return WebTest.executePostTestWithJson(webTestClient, "/login", EMPTY_COOKIE)
                .body(Mono.just(userLoginDto), UserLoginDto.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}