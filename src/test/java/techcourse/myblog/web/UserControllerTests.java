package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
public class UserControllerTests extends ControllerTemplate {

    @BeforeEach
    void setUp() {
        requestSignUp(NAME, EMAIL, PASSWORD);
    }

    @Test
    void loginForm() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void signUpForm() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 유저_동일한_email() {
        String anotherName = "kim";
        requestSignUp(anotherName, EMAIL, PASSWORD)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains("중복된 이메일입니다!"));
                })
        ;
    }

    @Test
    void 로그인후_메인화면() {
        requestLogin(EMAIL, PASSWORD)
                .expectStatus()
                .is3xxRedirection()
        ;
    }

    @Test
    void 로그인실패_이메일이_없는_경우() {
        String notFoundEmail = "nothing@gmail.com";
        requestLogin(notFoundEmail, PASSWORD)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains("일치하는 email이 없습니다!"));
                })
        ;
    }

    @Test
    void 로그인실패_비밀번호가_틀린_경우() {
        String notFoundPassword = "Password111!";
        requestLogin(EMAIL, notFoundPassword)
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertTrue(body.contains("비밀번호가 일치하지 않습니다!"));
                })
        ;
    }

    @Test
    void 로그인안하고_userlist_페이지접근시_로그인화면으로_이동() {
        webTestClient.get().uri("/users")
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/login")
                .expectStatus()
                .isFound()
        ; // 로그인 화면으로 갈 것임
    }

    @Test
    void 로그인안하고_myPage_페이지접근_로그인화면으로_이동() {
        webTestClient.get().uri("/mypage")
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/login")
                .expectStatus()
                .isFound()
        ; // 로그인 화면으로 갈 것임
    }

    @Test
    void 로그인안하고_myPage_Edit_페이지접근_로그인화면으로_이동() {
        webTestClient.get().uri("/mypage-edit")
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/login")
                .expectStatus()
                .isFound()
        ; // 로그인 화면으로 갈 것임
    }

    @Test
    void 로그인_후_userlist_정상_이동() {
        String cookie = getCookie(EMAIL, PASSWORD);
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 로그인_후_mypage_정상_이동() {
        String cookie = getCookie(EMAIL, PASSWORD);
        webTestClient.get().uri("/mypage")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 로그인_후_mypageedit_정상_이동() {
        String cookie = getCookie(EMAIL, PASSWORD);
        webTestClient.get().uri("/mypage-edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void 회원_이름_정상_수정() {
        webTestClient.put().uri("/users/1")
                .body(BodyInserters.fromFormData("name", "editName"))
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    webTestClient.get().uri(response.getResponseHeaders().getLocation())
                            .exchange()
                            .expectBody()
                            .consumeWith(response2 -> {
                                String body = new String(response2.getResponseBody());
                                assertTrue(body.contains("editName"));
                            })
                    ;
                })
        ;
    }

    @Test
    void 회원_정상_탈퇴() {
        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".*/")
        ;
    }
}
