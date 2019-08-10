package techcourse.myblog.web.controller.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;

import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    protected static UserDto savedUserDto = new UserDto("savedName", "saved@email.com", "Passw0rd!");
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;
    protected UserDto otherUserDto = new UserDto("other", "other@mail.com", "Passw0rd!");
    protected User savedUser;

    @BeforeEach
    protected void setup() {
        userRepository.save(otherUserDto.toUser());
        savedUser = userRepository.save(savedUserDto.toUser());
    }

    @AfterEach
    protected void tearDown() {
        userRepository.deleteAll();
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri) {
        return httpRequest(makeRequestSpec(method, uri));
    }

    protected StatusAssertions httpRequest(HttpMethod method, String uri,
                                           MultiValueMap<String, String> data) {
        return httpRequest(makeRequestSpec(method, uri, data));
    }

    private StatusAssertions httpRequest(RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .exchange()
                .expectStatus();
    }

    private WebTestClient.RequestBodySpec makeRequestSpec(HttpMethod method, String uri) {
        return webTestClient.method(method).uri(uri);
    }

    private RequestHeadersSpec<?> makeRequestSpec(HttpMethod method, String uri,
                                                  MultiValueMap<String, String> data) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    protected StatusAssertions loginAndRequest(UserDto loginUserDto, HttpMethod method, String path,
                                               MultiValueMap<String, String> data) {
        return httpRequest(
                makeRequestSpec(method, path, data)
                        .cookie("JSESSIONID", getLoginSessionId(loginUserDto)));
    }

    protected StatusAssertions loginAndRequest(UserDto loginUserDto, HttpMethod method, String path) {
        return httpRequest(
                makeRequestSpec(method, path)
                        .cookie("JSESSIONID", getLoginSessionId(loginUserDto)));
    }

    protected String getLoginSessionId(UserDto userDto) {
        return Objects.requireNonNull(httpRequest(POST, "/login", parseUser(userDto))
                .isFound()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("JSESSIONID"))
                .getValue();
    }

    protected String getResponseBody(StatusAssertions statusAssertions) {
        return new String(Objects.requireNonNull(statusAssertions
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody()));
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
