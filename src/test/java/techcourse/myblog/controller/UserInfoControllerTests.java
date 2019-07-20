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
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.dto.UserDto;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoControllerTests extends ControllerTestTemplate {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void 회원가입_성공_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*login");
    }

    @Test
    void 로그인되지_않은_경우_마이페이지_이동() {
        checkStatus(HttpMethod.GET, "/mypage", HttpStatus.FOUND);
    }

    @Test
    void 로그인_된_경우_마이페이지_이동() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        request(HttpMethod.GET, "/mypage")
                .cookie("JSESSIONID", getJsessionid(userDto))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

    @Test
    void 로그인되지_않은_경우_수정페이지_이동() {
        checkStatus(HttpMethod.GET, "/mypage/edit", HttpStatus.FOUND);
    }

    @Test
    void 로그인_된_경우_수정페이지_이동() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        request(HttpMethod.GET, "/mypage/edit")
                .cookie("JSESSIONID", getJsessionid(userDto))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

    @Test
    void 로그인되지_않은_경우_수정() {
        checkStatusAndHeaderLocation(response(HttpMethod.PUT, "/mypage/edit"), HttpStatus.FOUND, ".*/login");
    }

    @Test
    void 로그인_된_경우_수정_성공() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        UserDto updateUserDto = new UserDto("mirGood", null, null);
        request(HttpMethod.PUT, "/mypage/edit")
                .cookie("JSESSIONID", getJsessionid(userDto))
                .body(BodyInserters.fromFormData(bodyInsert(updateUserDto)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FOUND)
                .expectHeader().valueMatches("location", ".*/mypage");
    }

    @Test
    void 로그인_된_경우_수정_실패() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        UserDto updateUserDto = new UserDto("mir0422", null, null);
        request(HttpMethod.PUT, "/mypage/edit")
                .cookie("JSESSIONID", getJsessionid(userDto))
                .body(BodyInserters.fromFormData(bodyInsert(updateUserDto)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
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
