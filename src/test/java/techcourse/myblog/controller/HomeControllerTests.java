package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import techcourse.myblog.controller.template.ControllerTestTemplate;

public class HomeControllerTests extends ControllerTestTemplate {
    @Test
    void 홈페이지_이동_확인(){
        checkStatus(HttpMethod.GET, "/", HttpStatus.OK);
    }

    @Test
    void 게시글_작성_페이지_확인() {
        checkStatus(HttpMethod.GET, "/writing", HttpStatus.OK);
    }

    @Test
    void 로그인_페이지_확인() {
        checkStatus(HttpMethod.GET, "/login", HttpStatus.OK);
    }

    @Test
    void 회원가입_페이지_확인() {
        checkStatus(HttpMethod.GET, "/signup", HttpStatus.OK);
    }
}
