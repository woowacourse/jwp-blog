package techcourse.myblog.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;

import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;

import static org.springframework.http.HttpMethod.POST;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientGenerator {
    @Autowired
    private WebTestClient webTestClient;

    protected WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri) {
        return responseSpec(method, uri, new LinkedMultiValueMap<>());
    }

    protected WebTestClient.ResponseSpec responseSpec(HttpMethod method, String uri,
                                                      MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    protected WebTestClient.ResponseSpec logInResponseSpec(HttpMethod method, String uri, UserDto userDto, MultiValueMap<String, String> data) {
        MultiValueMap<String, ResponseCookie> cookies
                = responseSpec(POST, "/login", parser(userDto))
                .expectStatus()
                .isFound()
                .returnResult(Void.class)
                .getResponseCookies();

        return requestWithCookie(method, uri, data)
                .cookie("JSESSIONID", Objects.requireNonNull(cookies.getFirst("JSESSIONID")).getValue())
                .exchange();
    }

    public MultiValueMap<String, ResponseCookie> getLoginCookie(UserDto userDto) {
        return responseSpec(POST, "/login", parser(userDto))
                .returnResult(Void.class)
                .getResponseCookies();
    }

    private WebTestClient.RequestHeadersSpec<?> requestWithCookie(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data));
    }

    protected WebTestClient.ResponseSpec logInResponseSpec(HttpMethod method, String uri, UserDto userDto) {
        return logInResponseSpec(method, uri, userDto, new LinkedMultiValueMap<>());
    }

    protected MultiValueMap<String, String> parser(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }

    protected String responseBody(WebTestClient.ResponseSpec responseSpec) {
        return responseSpec
                .expectBody()
                .returnResult()
                .toString();
    }
    protected String getRedirectedUri(EntityExchangeResult<byte[]> response) {
        return response.getResponseHeaders().get("Location").get(0);
    }

    protected MultiValueMap<String, String> parser(CommentDto commentDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("contents", commentDto.getContents());
        return multiValueMap;
    }

    protected MultiValueMap<String, String> parser(ArticleDto articleDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", articleDto.getTitle());
        multiValueMap.add("coverUrl", articleDto.getCoverUrl());
        multiValueMap.add("contents", articleDto.getContents());
        return multiValueMap;
    }
}
