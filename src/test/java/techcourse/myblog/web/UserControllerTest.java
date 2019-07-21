package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.function.Consumer;

// TODO 회원가입을 테스트해야 하는데 메서드가 무작위로 실행돼서 회원이 이미 등록된 상태에서 실행되는 문제는 어떡한담

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String CUSTOM_USER_ID = "1";
    private static final String VALID_NAME = "유효한이름";
    private static final String VALID_EMAIL = "valid@email.com";
    private static final String VALID_PASSWORD = "ValidPassword!123";

    private BodyInserters.FormInserter<String> validUserData = getBodyInserters(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void 회원가입_페이지_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 유효한_정보로_회원가입_하는_경우_테스트() {
        postSignup(validUserData, response ->
                webTestClient.get()
                        .uri(response.getResponseHeaders().getLocation())
                        .exchange()
                        .expectStatus()
                        .isOk()
        );
    }

    @Test
    public void 유효햐지_않은_이름으로_회원가입_하는_경우_예외처리() {
        String invalidName = "1nva1id";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(invalidName, VALID_EMAIL, VALID_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 유효하지_않은_이메일로_회원가입_하는_경우_예외처리() {
        String invalidEmail = "invalidemail";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(VALID_NAME, invalidEmail, VALID_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 유효하지_않은_비밀번호로_회원가입_하는_경우_예외처리() {
        String invalidPassword = "invalidpw";

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBodyInserters(VALID_NAME, VALID_EMAIL, invalidPassword))
                .exchange()
                .expectStatus().isBadRequest();
    }

//    @Test
//    public void 회원수정_페이지가_잘_출력되는지_테스트() {
//        /** 회원가입 **/
//        postSignup(validUserData, response ->
//                /** TODO 로그인 **/
//                /** 회원수정 **/
//                webTestClient.get()
//                        .uri("/mypage/edit")
//                        .exchange()
//                        .expectStatus().isOk()
//                        .expectBody()
//                        .consumeWith(res -> {
//                            String body = new String(res.getResponseBody());
//                            assertThat(body.contains(VALID_NAME)).isTrue();
//                            assertThat(body.contains(VALID_EMAIL)).isTrue();
//                        })
//        );
//    }

    @Test
    public void 로그인이_안된_상태에서_회원수정_페이지에_접속하는_경우_예외처리() {
        webTestClient.get()
                .uri("/mypage/edit")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 회원정보_수정_테스트() {
        String name = "효진쓰";
        String password = "@Password123";

        /** 회원가입 **/
        postSignup(validUserData, response ->
                /** TODO 로그인 **/
                /** 회원정보 수정 **/
                webTestClient.put()
                        .uri("/mypage/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(getBodyInserters(name, VALID_EMAIL, password))
                        .exchange()
                        .expectStatus().isFound()
        );
    }

//    @Test
//    public void 회원조회_페이지가_잘_출력되는지_테스트() {
//        /** 회원가입 **/
//        postSignup(validUserData, response ->
//                /** TODO 로그인 **/
//                /** 회원조회 **/
//                webTestClient.get()
//                        .uri("/users")
//                        .exchange()
//                        .expectStatus()
//                        .isOk()
//                        .expectBody()
//                        .consumeWith(
//                                res -> {
//                                    String body = new String(res.getResponseBody());
//                                    assertThat(body.contains(VALID_NAME)).isTrue();
//                                    assertThat(body.contains(VALID_EMAIL)).isTrue();
//                                }
//                        )
//        );
//    }

    @Test
    public void 로그인이_안된_상태에서_회원조회_페이지에_접속하는_경우_예외처리() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void 회원탈퇴_테스트() {
        /** 회원가입 **/
        postSignup(validUserData, response ->
                /** TODO 로그인 **/
                /** 회원탈퇴 **/
                webTestClient.delete()
                        .uri("/mypage/edit/{userId}", CUSTOM_USER_ID)
                        .attribute("", VALID_EMAIL)
                        .exchange()
                        .expectStatus().isFound()
        );
    }

    private void postSignup(BodyInserters.FormInserter<String> userData, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(userData)
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(consumer);
    }

    private BodyInserters.FormInserter<String> getBodyInserters(String name, String email, String password) {
        return BodyInserters.fromFormData("userId", CUSTOM_USER_ID)
                .with("name", name)
                .with("email", email)
                .with("password", password);
    }
}