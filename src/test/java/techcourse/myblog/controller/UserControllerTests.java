package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import techcourse.myblog.controller.common.UserCommonTests;
import techcourse.myblog.service.dto.UserDto;

public class UserControllerTests extends UserCommonTests {
    @Test
    void 회원가입_중복_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusWithData(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)), HttpStatus.FOUND);
    }

    @Test
    void 회원가입_실패_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@");
        checkStatusWithData(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)), HttpStatus.FOUND);
    }

    @Test
    void 회원_조회_이동_확인() {
        checkStatus(HttpMethod.GET, "/users", HttpStatus.OK);
    }

    @Test
    void 로그인_성공_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users/login", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*/");
    }

    @Test
    void 로그인_실패_확인() {
        UserDto userDto = new UserDto("mir", "mir0422@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users/login", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*/login");
    }
}
