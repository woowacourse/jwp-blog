package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerTests extends ControllerTestTemplate {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private String name;
    private String email;
    private String password;
    private UserDto savedUserDto;

    @BeforeEach
    void setup() {
        name = "name";
        email = "email@email";
        password = "password";
        savedUserDto = new UserDto("savedName", "saved@email", "savedPassword");
        userRepository.save(savedUserDto.toUser());
    }

    @Test
    void 로그인_페이지_요청() {
        requestExpect(GET, "/login").isOk();
    }

    @Test
    void 회원목록_페이지_요청() {
        requestExpect(GET, "/users").isOk();
    }

    @Test
    void 로그아웃상태_회원가입_페이지_요청() {
        requestExpect(GET, "/signup").isOk();
    }

    @Test
    void 로그아웃_요청() {
        loginAndRequest(savedUserDto, "/logout").isFound();
    }

    @Test
    void 비로그인시_회원_정보_페이지_요청_불가() {
        requestExpect(GET, "/mypage").isFound();
    }

    @Test
    void 로그인_회원_정보_페이지_요청() {
        loginAndRequest(savedUserDto, "/mypage").isOk();
    }

    @Test
    void 비로그인시_회원_정보_수정_페이지_요청_불가() {
        requestExpect(GET, "/mypage/edit").isFound();
    }

    @Test
    void 회원가입_성공시_리다이렉트() {
        UserDto userDto = new UserDto(name, email, password);
        requestExpect(POST, "/users", parser(userDto)).isFound();
    }

    @Test
    void 이름_길이_규칙_위반_회원가입_실패() {
        name = "k";
        UserDto userDto = new UserDto(name, email, password);

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/users", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다."));
    }

    @Test
    void 이메일_형식_위반_회원가입_실패() {
        email = "email";
        UserDto userDto = new UserDto(name, email, password);

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/users", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("이메일 양식을 지켜주세요."));
    }

    @Test
    void 비밀번호_길이_규칙_위반_회원가입_실패() {
        password = "1234567";
        UserDto userDto = new UserDto(name, email, password);

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/users", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요."));
    }

    @Test
    void 동일_이메일_중복_가입() {
        UserDto userDto = new UserDto(name, savedUserDto.getEmail(), password);

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/users", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("이미 존재하는 email입니다."));
    }

    @Test
    void 회원목록_페이지() {
        requestExpect(GET, "/users").isOk();
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        requestExpect(POST, "/login", parser(savedUserDto)).isFound();
    }

    @Test
    void 등록되지_않은_이메일로_로그인시_에러() {
        UserDto userDto = new UserDto(null, "wrong@email", password);

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/login", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("이메일을 확인해주세요."));
    }

    @Test
    void 비밀번호_불일치하는_경우_에러() {
        UserDto userDto = new UserDto(null, savedUserDto.getEmail(), "wrongpassword");

        WebTestClient.ResponseSpec responseBody = requestExpect(POST, "/login", parser(userDto)).isOk();
        bodyCheck(responseBody, Arrays.asList("비밀번호를 확인해주세요."));
    }

    @Test
    void 로그인_상태에서_회원가입_요청시_리다이렉트() {
        loginAndRequest(savedUserDto, "/signup").isFound();
    }

    @Test
    void 로그인_상태에서_로그인_페이지_요청시_리다이렉트() {
        loginAndRequest(savedUserDto, "/login").isFound();
    }

    @Test
    void 로그인_상태에서_마이페이지_요청시_성공() {
        loginAndRequest(savedUserDto, "/mypage").isOk();
    }

    @Test
    void 로그인_상태에서_정보수정페이지_요청시_성공() {
        loginAndRequest(savedUserDto, "/mypage/edit").isOk();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private StatusAssertions loginAndRequest(UserDto userDto, String path) {
        String loginSessionId = requestExpect(POST, "/login", parser(userDto))
                .isFound()
                .returnResult(Void.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();

        return requestExpect(
                makeRequestSpec(GET, path)
                        .cookie("JSESSIONID", loginSessionId));
    }

    private MultiValueMap<String, String> parser(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }
}