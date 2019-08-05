package techcourse.myblog.presentation.controller.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import reactor.core.publisher.Mono;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.application.dto.ArticleDto;

import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import static techcourse.myblog.utils.UserTestObjects.SIGN_UP_USER_DTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    private static final String JSESSIONID = "JSESSIONID";
    public static final String LOGIN_URL = "/login";
    
    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    protected UserDto savedUserDto;
    protected User savedUser;

    @BeforeEach
    protected void setup() {
        savedUserDto = SIGN_UP_USER_DTO;
        savedUser = userRepository.save(savedUserDto.toUser());
    }

    @AfterEach
    protected void tearDown() {
        userRepository.deleteAll();
    }
    
    protected StatusAssertions httpRequest(HttpMethod method, String uri) {
        return httpRequest(makeRequestSpec(method, uri));
    }

    protected StatusAssertions httpRequestWithData(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return httpRequest(makeRequestSpecWithData(method, uri, data));
    }
    
    protected StatusAssertions loginAndRequest(HttpMethod method, String path, UserDto userDto) {
        return httpRequest(makeRequestSpec(method, path).cookie(JSESSIONID, getLoginSessionId(userDto)));
    }

    protected StatusAssertions loginAndRequestWithData(HttpMethod method, String path, MultiValueMap<String, String> data, UserDto userDto) {
        return httpRequest(makeRequestSpecWithData(method, path, data).cookie(JSESSIONID, getLoginSessionId(userDto)));
    }
    
    protected StatusAssertions loginAndRequestWriter(HttpMethod method, String path) {
        return loginAndRequest(method, path, savedUserDto);
    }
    
    protected StatusAssertions loginAndRequestWithDataWriter(HttpMethod method, String path, MultiValueMap<String, String> data) {
        return loginAndRequestWithData(method, path, data, savedUserDto);
    }
    
    protected String getLoginSessionId(UserDto userDto) {
        return Objects.requireNonNull(httpRequestWithData(POST, LOGIN_URL, parseUser(userDto))
                .isFound()
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst(JSESSIONID))
                .getValue();
    }
    
    private StatusAssertions httpRequest(RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .exchange()
                .expectStatus();
    }
    
    private RequestBodySpec makeRequestSpec(HttpMethod method, String uri) {
        return webTestClient.method(method).uri(uri);
    }
    
    private RequestHeadersSpec<?> makeRequestSpecWithData(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
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
