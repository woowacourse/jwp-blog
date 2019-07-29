package techcourse.myblog.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;

import java.io.UnsupportedEncodingException;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class Utils {
    public static String getResponseBody(byte[] body) {
        try {
            return new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("인코딩 에러");
        }
    }

    public static void createUser(WebTestClient webTestClient, UserDto userDto) {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", userDto.getUserName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange()
                .expectStatus().isFound();
    }

    public static String getLoginCookie(WebTestClient webTestClient, LoginDto loginDto) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
    }
}
