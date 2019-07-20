package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.service.dto.UserDto;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests extends ControllerTestTemplate {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void 회원가입_성공_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*login");
    }

    @Test
    void 회원가입_중복_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusWithData(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)), HttpStatus.OK);
    }

    @Test
    void 회원가입_실패_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@");
        checkStatusWithData(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)), HttpStatus.OK);
    }

    @Test
    void 회원_조회_이동_확인() {
        checkStatus(HttpMethod.GET, "/users", HttpStatus.OK);
    }

    @Test
    void 로그인_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users/login", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*/.*");
    }

    @AfterEach
    void 회원_탈퇴() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");

        request(HttpMethod.DELETE, "/mypage/withdraw")
                .cookie("JSESSIONID", getJsessionid(userDto))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND)
                .expectHeader().valueMatches("location", ".*/users/logout");
    }

    private String getJsessionid(UserDto userDto) {
        return checkStatusWithData(responseWithData(HttpMethod.POST, "/users/login", bodyInsert(userDto)), HttpStatus.FOUND)
                .returnResult(Void.class)
                .getResponseCookies()
                .getFirst("JSESSIONID")
                .getValue();
    }

    private MultiValueMap<String, String> bodyInsert(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }
}
