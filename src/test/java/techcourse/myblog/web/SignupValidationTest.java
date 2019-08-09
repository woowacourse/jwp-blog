package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.dto.UserDto.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupValidationTest {
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String TEST_EMAIL = "sean@gmail.com";
    private static final String TEST_NAME = "sean!";
    private static final String TEST_PASSWORD = "Woowahan123!";
    private static final String USER_URL = "/users";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void name이_2자_미만인_경우() {
        checkValidationWith(NAME_LENGTH_ERROR, "s", TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void name이_10자_초과인_경우() {
        checkValidationWith(NAME_LENGTH_ERROR, "qwerasdfzxc", TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void name에_숫자가_포함된_경우() {
        checkValidationWith(NAME_FORMAT_ERROR, "sean123", TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void name에_특수문자가_포함된_경우() {
        checkValidationWith(NAME_FORMAT_ERROR, TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void name에_공백이_입력된_경우() {
        checkValidationWith(NAME_BLANK_ERROR, " ", TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void email_양식이_잘못된_경우() {
        checkValidationWith(EMAIL_FORMAT_ERROR, TEST_NAME, "sean_gmail.com", TEST_PASSWORD);
    }

    @Test
    void email이_공백인_경우() {
        checkValidationWith(EMAIL_BLANK_ERROR, TEST_NAME, "", TEST_PASSWORD);
    }

    @Test
    void password에_소문자가_포함되지_않은_경우() {
        checkValidationWith(PASSWORD_FORMAT_ERROR, TEST_NAME, TEST_EMAIL, "WOOWAHAN123!");
    }

    @Test
    void password에_대문자가_포함되지_않은_경우() {
        checkValidationWith(PASSWORD_FORMAT_ERROR, TEST_NAME, TEST_EMAIL, "woowahan123!");
    }

    @Test
    void password에_숫자가_포함되지_않은_경우() {
        checkValidationWith(PASSWORD_FORMAT_ERROR, TEST_NAME, TEST_EMAIL, "Woowahan!");
    }

    @Test
    void password에_특수문자가_포함되지_않은_경우() {
        checkValidationWith(PASSWORD_FORMAT_ERROR, TEST_NAME, TEST_EMAIL, "Woowahan123");
    }

    @Test
    void password에_공백이_입력된_경우() {
        checkValidationWith(PASSWORD_BLANK_ERROR, TEST_NAME, TEST_EMAIL, " ");
    }

    private void checkValidationWith(String errorMessage, String name, String email, String password) {
        webTestClient.post()
                .uri(USER_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData(NAME, name)
                        .with(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(errorMessage)).isTrue();
        });
    }
}
