package techcourse.myblog.web.controller.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.UserDto;

import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    protected String name;
    protected String email;
    protected String password;
    protected UserDto savedUserDto;

    protected User savedUser;

    @BeforeEach
    protected void setup() {
        name = "name";
        email = "email@email.com";
        password = "passw0RD!";
        savedUserDto = new UserDto("savedName", "saved@email.com", "savedPassw0RD!");
        savedUser = userRepository.save(savedUserDto.toUser());
    }

    @AfterEach
    protected void tearDown() {
        userRepository.deleteAll();
    }

    protected StatusAssertions httpRequest(RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .exchange()
                .expectStatus();
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri) {
        return httpRequest(makeRequestSpec(method, uri));
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri,
                                           MultiValueMap<String, String> data) {
        return httpRequest(makeRequestSpec(method, uri, data));
    }

    protected WebTestClient.RequestBodySpec makeRequestSpec(HttpMethod method, String uri) {
        return webTestClient.method(method).uri(uri);
    }

    protected RequestHeadersSpec<?> makeRequestSpec(HttpMethod method, String uri,
                                                    MultiValueMap<String, String> data) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    protected StatusAssertions loginAndRequest(HttpMethod method, String path, MultiValueMap<String, String> data, UserDto userDto) {
        return httpRequest(
                makeRequestSpec(method, path, data)
                        .cookie("JSESSIONID", getLoginSessionId(userDto)));
    }

    protected StatusAssertions loginAndRequest(HttpMethod method, String path, UserDto userDto) {
        return httpRequest(
                makeRequestSpec(method, path)
                        .cookie("JSESSIONID", getLoginSessionId(userDto)));
    }


    protected StatusAssertions loginAndRequest(HttpMethod method, String path, MultiValueMap<String, String> data) {
        return loginAndRequest(method, path, data, savedUserDto);
    }

    protected StatusAssertions loginAndRequest(HttpMethod method, String path) {
        return loginAndRequest(method, path, savedUserDto);
    }

    private String getLoginSessionId(UserDto userDto) {
        return Objects.requireNonNull(httpRequest(POST, "/login", parseUser(userDto))
                .isFound()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID"))
                .getValue();
    }

    protected String getResponseBody(StatusAssertions statusAssertions) {
        return new String(statusAssertions
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody());
    }

    protected String getRedirectUrl(StatusAssertions statusAssertions) {
        return statusAssertions
                .isFound()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation().getPath();
    }

    protected MultiValueMap<String, String> parseUser(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }


    protected MultiValueMap<String, String> parseArticle(ArticleDto articleDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", articleDto.getTitle());
        multiValueMap.add("coverUrl", articleDto.getCoverUrl());
        multiValueMap.add("contents", articleDto.getContents());
        return multiValueMap;
    }
}
