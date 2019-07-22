package techcourse.myblog.controller.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import techcourse.myblog.controller.template.ControllerTestTemplate;
import techcourse.myblog.service.dto.UserDto;

public class UserCommonTests extends ControllerTestTemplate {
    @BeforeEach
    void 회원가입_성공_확인() {
        UserDto userDto = new UserDto("mir", "mir@naver.com", "1234!@Aa");
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/users", bodyInsert(userDto)),
                HttpStatus.FOUND, ".*/login");
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
}
