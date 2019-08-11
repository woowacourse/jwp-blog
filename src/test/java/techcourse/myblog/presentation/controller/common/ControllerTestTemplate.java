package techcourse.myblog.presentation.controller.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.application.UserAssembler;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.application.dto.UserResponseDto;
import techcourse.myblog.domain.article.ArticleFeature;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import static techcourse.myblog.utils.UserTestObjects.SIGN_UP_USER_DTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class ControllerTestTemplate {
    protected static final String MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE = "댓글 작성자가 아닙니다.";
    protected static final String MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE = "게시글 작성자가 아닙니다.";
    protected static final String DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다.";
    protected static final String NOT_FOUND_ARTICLE_EXCEPTION_MESSAGE = "존재하지 않는 게시글입니다.";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String LOGIN_URL = "/login";

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    protected UserRequestDto savedUserRequestDto;
    protected UserResponseDto savedUser;

    @BeforeEach
    protected void setup() {
        savedUserRequestDto = SIGN_UP_USER_DTO;
        savedUser = UserAssembler.buildUserResponseDto(userRepository.save(UserAssembler.toEntity(savedUserRequestDto)));
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

    protected StatusAssertions loginAndRequest(HttpMethod method, String path, UserRequestDto userRequestDto) {
        return httpRequest(makeRequestSpec(method, path).cookie(JSESSIONID, getLoginSessionId(userRequestDto)));
    }

    protected StatusAssertions loginAndRequestWithData(HttpMethod method, String path, MultiValueMap<String, String> data, UserRequestDto userRequestDto) {
        return httpRequest(makeRequestSpecWithData(method, path, data).cookie(JSESSIONID, getLoginSessionId(userRequestDto)));
    }

    protected BodyContentSpec loginAndRequestWithData(HttpMethod method, String path, HttpStatus httpStatus, MultiValueMap<String, String> data, UserRequestDto userRequestDto) {
        return httpRequest(makeRequestSpecWithData(method, path, data).cookie(JSESSIONID, getLoginSessionId(userRequestDto)), httpStatus);
    }

    protected BodyContentSpec loginAndRequest(HttpMethod method, String path, HttpStatus httpStatus, UserRequestDto userRequestDto) {
        return httpRequest(makeRequestSpec(method, path).cookie(JSESSIONID, getLoginSessionId(userRequestDto)), httpStatus);
    }

    protected BodyContentSpec loginAndRequestWithMonoData(HttpMethod method, String path, HttpStatus httpStatus, Object object, UserRequestDto userRequestDto) {
        return httpRequest(makeRequestSpecWithData(method, path, object).cookie(JSESSIONID, getLoginSessionId(userRequestDto)), httpStatus);
    }

    protected StatusAssertions loginAndRequestWriter(HttpMethod method, String path) {
        return loginAndRequest(method, path, savedUserRequestDto);
    }
    
    protected StatusAssertions loginAndRequestWithDataWriter(HttpMethod method, String path, MultiValueMap<String, String> data) {
        return loginAndRequestWithData(method, path, data, savedUserRequestDto);
    }
    
    protected String getLoginSessionId(UserRequestDto userRequestDto) {
        return Objects.requireNonNull(httpRequestWithData(POST, LOGIN_URL, parseUser(userRequestDto))
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

    private BodyContentSpec httpRequest(RequestHeadersSpec requestHeadersSpec, HttpStatus httpStatus) {
        return requestHeadersSpec
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody();
    }
    
    private RequestBodySpec makeRequestSpec(HttpMethod method, String uri) {
        return webTestClient.method(method).uri(uri);
    }
    
    private RequestHeadersSpec<?> makeRequestSpecWithData(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    private RequestHeadersSpec<?> makeRequestSpecWithData(HttpMethod method, String uri, Object object) {
        return makeRequestSpec(method, uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(object), Object.class);
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

    protected MultiValueMap<String, String> parseUser(UserRequestDto userRequestDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userRequestDto.getEmail());
        multiValueMap.add("name", userRequestDto.getName());
        multiValueMap.add("password", userRequestDto.getPassword());
        return multiValueMap;
    }


    protected MultiValueMap<String, String> parseArticle(ArticleFeature articleFeature) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", articleFeature.getTitle());
        multiValueMap.add("coverUrl", articleFeature.getCoverUrl());
        multiValueMap.add("contents", articleFeature.getContents());
        return multiValueMap;
    }
}
