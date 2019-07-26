package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.controller.common.UserCommonTests;
import techcourse.myblog.service.dto.UserDto;

public class UserInfoControllerTests extends UserCommonTests {
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
                .expectStatus().isEqualTo(HttpStatus.FOUND);
    }
}
