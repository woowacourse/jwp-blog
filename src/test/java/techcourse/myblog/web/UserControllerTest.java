package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserDto;

import static techcourse.myblog.web.AuthControllerTest.*;
import static techcourse.myblog.web.UserController.USER_DEFAULT_URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private UserDto testUserDto;

    static WebTestClient.ResponseSpec 회원_등록(WebTestClient webTestClient, UserDto userDto) {
        return webTestClient.post().uri(USER_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("name", userDto.getName())
                                .with("email", userDto.getEmail())
                                .with("password", userDto.getPassword())
                ).exchange();
    }

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto();
        testUserDto.setName("pkch");
        testUserDto.setEmail("pkch@woowa.com");
        testUserDto.setPassword("!234Qwer");
    }

    @Test
    void 회원가입_페이지_접근_테스트() {
        webTestClient.get().uri(USER_DEFAULT_URL + "/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 회원_목록_페이지_접근_테스트() {
        webTestClient.get().uri(USER_DEFAULT_URL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 같은_이메일의_회원이_등록되는_경우_회원가입_페이지_리다이렉팅_테스트() {
        회원_등록(webTestClient, testUserDto);
        회원_등록(webTestClient, testUserDto).expectStatus().isFound()
                .expectHeader()
                .valueMatches("Location", ".*/users/signup");
    }

    @Test
    void 회원가입시_이름이_10자_넘어가는_경우_회원가입_페이지_리다이렉팅_테스트() {
        testUserDto.setName("내이름은열글자가넘어요");
        회원_등록(webTestClient, testUserDto)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/users/signup");
    }

    @Test
    void 회원가입시_비밀번호가_8자_이하인_경우_회원가입_페이지_리다이렉팅_테스트() {
        testUserDto.setPassword("hellopk");
        회원_등록(webTestClient, testUserDto)
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/users/signup");
    }

    @Test
    void 로그인_된_상태에서_회원가입_페이지_접근시_메인_페이지_리다이텍팅_테스트() {
        회원_등록(webTestClient, testUserDto);
        String sessionId = 로그인_세션_ID(webTestClient, testEmail, testPassword);
        webTestClient.get().uri(USER_DEFAULT_URL + "/signup")
                .header("cookie", sessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 로그인_상태에서_마이페이지_접근_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.get().uri(USER_DEFAULT_URL + "/pkch@woowa.com")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 비로그인_상태에서_임의의_email로_마이페이지_접근시_로그인페이지_리다이렉팅_테스트() {
        webTestClient.get().uri(USER_DEFAULT_URL + "/pkch@woowa.com")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/auth/login");
    }

    @Test
    void 로그인_상태에서_로그인_된_이메일이_아닌_다른_이메일uri로_접근시_메인페이지_리다이렉팅_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.get().uri(USER_DEFAULT_URL + "/park@woowa.com")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 로그인_상태에서_개인정보_수정페이지_접근_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.get().uri(USER_DEFAULT_URL + "/pkch@woowa.com/edit")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 비로그인_상태에서_임의의_email로_개인정보_수정페이지_접근시_로그인페이지_리다이렉팅_테스트() {
        webTestClient.get().uri(USER_DEFAULT_URL + "/pkch@woowa.com/edit")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/auth/login");
    }

    @Test
    void 로그인_상태에서_로그인_된_이메일이_아닌_다른_이메일uri로_수정페이지_접근시_메인페이지_리다이렉팅_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.get().uri(USER_DEFAULT_URL + "/park@woowa.com/edit")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 수정_정상_흐름_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.put().uri(USER_DEFAULT_URL + "/pkch@woowa.com")
                .header("cookie", jSessionId)
                .body(BodyInserters.fromFormData("name", "park"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/users/pkch@woowa.com");
    }

    @Test
    void 비로그인_상태에서_수정_요청시_로그인_페이지_리다이렉팅_테스트() {
        webTestClient.put().uri(USER_DEFAULT_URL + "/park@woowa.com")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/auth/login");
    }

    @Test
    void 회원_탈퇴_정상_흐름_테스트() {
        회원_등록(webTestClient, testUserDto);
        String jSessionId = 로그인_세션_ID(webTestClient, "pkch@woowa.com", "!234Qwer");
        webTestClient.delete().uri(USER_DEFAULT_URL + "/pkch@woowa.com")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/");
    }

    @Test
    void 비로그인_상테에서_회원_탈퇴시_로그인_페이지_리다이렉팅_테스트() {
        webTestClient.delete().uri(USER_DEFAULT_URL + "/pkch@woowa.com")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/auth/login");
    }
}