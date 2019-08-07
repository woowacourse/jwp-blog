package techcourse.myblog.web;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.UserVO;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private String cookie;
    private static final String TEST_EMAIL = "abc@hi.com";
    private static final String TEST_PW = "abcdEFGH123!@#";

    @BeforeEach
    void setUp() {
        loginRequest(TEST_EMAIL, TEST_PW);
//        EntityExchangeResult<byte[]> result = postFormRequest("/login", UserVO.class, "", TEST_PW, TEST_EMAIL);
//        this.cookie = result
//                .getResponseHeaders()
//                .getFirst("Set-Cookie");
    }

    protected EntityExchangeResult<byte[]> getRequest(String uri) {
        return webTestClient.get()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> postJsonRequest(String uri, Class mappingClass, String... args) {
        Map<String, String> params = new HashMap();

        for (int i = 0; i < mappingClass.getDeclaredFields().length; i++) {
            params.put(mappingClass.getDeclaredFields()[i].getName(), args[i]);
        }

        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .body(Mono.just(params), Map.class)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> postFormRequest(String uri, Class mappingClass, String... args) {
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(mappingClass, args))
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> deleteRequest(String uri) {
        return webTestClient.delete()
                .uri(uri)
                .header("Cookie", cookie)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> putFormRequest(String uri, Class mappingClass, String... args) {
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapBy(mappingClass, args))
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected EntityExchangeResult<byte[]> putJsonRequest(String uri, Class mappingClass, String... args) {
        Map<String, String> params = new HashMap();

        for (int i = 0; i < mappingClass.getDeclaredFields().length; i++) {
            params.put(mappingClass.getDeclaredFields()[i].getName(), args[i]);
        }

        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .body(Mono.just(params), Map.class)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected void loginRequest(String email, String password) {
        EntityExchangeResult<byte[]> result = postFormRequest("/login", UserVO.class, "", password, email);
        this.cookie = result
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    private <T> BodyInserters.FormInserter<String> mapBy(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);

        for (int i = 0; i < classType.getDeclaredFields().length; i++) {
            body.with(classType.getDeclaredFields()[i].getName(), parameters[i]);
        }
        return body;
    }

    protected void assertTest(String body, String... args) {
        for (String arg : args) {
            assertThat(body.contains(arg)).isTrue();
        }
    }
}
