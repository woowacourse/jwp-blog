package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import techcourse.myblog.controller.test.WebClientGenerator;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(SpringExtension.class)
class UserControllerTest extends WebClientGenerator {

    @Autowired
    private UserRepository userRepository;

    private String name;
    private String email;
    private String password;

    @BeforeEach
    void setup() {
        name = "name";
        email = "email@email";
        password = "password";
    }

    @Test
    void 로그인_페이지_요청() {
        responseSpec(GET, "/login")
                .expectStatus()
                .isOk();
    }

    @Test
    void 회원목록_페이지_요청() {
        responseSpec(GET, "/users")
                .expectStatus()
                .isOk();
    }

    @Test
    void 회원가입_페이지_요청() {
        responseSpec(GET, "/signup")
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그아웃() {
        responseSpec(GET, "/logout")
                .expectStatus()
                .isFound();
    }

    @Test
    void 비로그인시_회원_정보_페이지_요청_불가() {
        responseSpec(GET, "/mypage")
                .expectStatus()
                .isFound();
    }

    @Test
    void 비로그인시_회원_정보_수정_페이지_요청_불가() {
        responseSpec(GET, "/mypage/edit")
                .expectStatus()
                .isFound();
    }

    @Test
    void 회원가입_성공() {
        UserDto userDto = new UserDto(name, email, password);

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    assertEquals(user.getEmail(), userDto.getEmail());
                    assertEquals(user.getName(), userDto.getName());
                    assertEquals(user.getPassword(), userDto.getPassword());
                });
    }

    @Test
    void 이름_길이_규칙_위반() {
        name = "k";
        UserDto userDto = new UserDto(name, email, password);

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.")
                );

        assertFalse(userRepository.findByEmail(email).isPresent());
    }

    private void assertRedirectBodyContains(EntityExchangeResult<byte[]> response, String message) {
        String redirectBody = responseBody(responseSpec(GET, getRedirectedUri(response)));
        assertTrue(redirectBody.contains(message));
    }

    @Test
    void 이메일_형식_위반() {
        email = "email";
        UserDto userDto = new UserDto(name, email, password);

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "이메일 양식을 지켜주세요.")
                );

        assertFalse(userRepository.findByEmail(email).isPresent());
    }

    @Test
    void 비밀번호_길이_규칙_위반() {
        password = "1234567";
        UserDto userDto = new UserDto(name, email, password);

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
                );

        assertFalse(userRepository.findByEmail(email).isPresent());
    }

    @Test
    void 동일_이메일_중복_가입() {
        email = "registered1@email";
        UserDto userDto = new UserDto(name, email, password);

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        assertTrue(userRepository.findByEmail(email).isPresent());

        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "이미 존재하는 email입니다.")
                );
    }

    @Test
    void 회원목록_페이지() {
        responseSpec(GET, "/users")
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        final String forLoginEmail = "registered2@email";

        UserDto userDto = new UserDto(name, forLoginEmail, password);
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        responseSpec(POST, "/login", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, name)
                );
    }

    @Test
    void 해당_이메일이_없는_경우_에러() {
        final String notSignedUpEmail = "thisisfake@login.com";

        UserDto userDto = new UserDto(name, notSignedUpEmail, password);

        responseSpec(POST, "/login", parser(userDto))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "로그인 정보를 확인해주세요.")
                );
    }

    @Test
    void 비밀번호_불일치하는_경우_에러() {
        final String forLoginEmail = "registered3@email";

        UserDto userDto = new UserDto(name, forLoginEmail, password);
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        UserDto userDtoWrongPassword = new UserDto(name, forLoginEmail, "abcdeghf");
        responseSpec(POST, "/login", parser(userDtoWrongPassword))
                .expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        assertRedirectBodyContains(response, "로그인 정보를 확인해주세요.")
                );
    }

    @Test
    void 로그인_상태에서_회원가입_요청시_리다이렉트() {
        UserDto userDto = new UserDto("루피", "luffy1@pirates.com", "12345678");
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        logInResponseSpec(GET, "/signup", userDto)
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_상태에서_로그인_페이지_요청시_리다이렉트() {
        UserDto userDto = new UserDto("루피", "luffy2@pirates.com", "12345678");
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        logInResponseSpec(GET, "/login", userDto)
                .expectStatus()
                .isFound();
    }

    @Test
    void 로그인_상태에서_마이페이지_요청시_성공() {
        UserDto userDto = new UserDto("루피", "luffy3@pirates.com", "12345678");
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        logInResponseSpec(GET, "/mypage", userDto)
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_상태에서_정보수정페이지_요청시_성공() {
        UserDto userDto = new UserDto("루피", "luffy4@pirates.com", "12345678");
        responseSpec(POST, "/users", parser(userDto))
                .expectStatus()
                .isFound();

        logInResponseSpec(GET, "/mypage/edit", userDto)
                .expectStatus()
                .isOk();
    }

    @AfterEach
    void 초기화() {
        userRepository.findByEmail(email).ifPresent(user -> userRepository.delete(user));
    }
}