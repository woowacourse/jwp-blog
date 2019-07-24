package techcourse.myblog.controller.template;

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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.UserDto;

import static org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;


@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestTemplate {
    private static final String LOCATION = "location";

    @Autowired
    private WebTestClient webTestClient;

    protected MultiValueMap<String, String> bodyInsert(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }

    protected String getJsessionid(UserDto userDto) {
        return checkStatusWithData(responseWithData(HttpMethod.POST, "/users/login", bodyInsert(userDto)), HttpStatus.FOUND)
                .returnResult(Void.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }

    protected RequestBodySpec request(HttpMethod method, String uri) {
        return webTestClient.method(method)
                .uri(uri);
    }

    protected ResponseSpec response(HttpMethod method, String uri) {
        return request(method, uri)
                .exchange();
    }

    protected ResponseSpec responseWithData(HttpMethod method, String uri, MultiValueMap<String, String> data) {
        return request(method, uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .exchange();
    }

    protected ResponseSpec checkStatus(HttpMethod method, String uri, HttpStatus status) {
        return response(method, uri)
                .expectStatus().isEqualTo(status);
    }

    protected ResponseSpec checkStatusWithData(ResponseSpec responseSpec, HttpStatus status) {
        return responseSpec
                .expectStatus().isEqualTo(status);
    }

    protected ResponseSpec checkStatusAndHeaderLocation(ResponseSpec responseSpec, HttpStatus status, String matcher) {
        return checkStatusWithData(responseSpec, status)
                .expectHeader().valueMatches(LOCATION, matcher);
    }
}
