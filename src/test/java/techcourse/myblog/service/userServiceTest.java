package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.exception.UnacceptablePathException;

import javax.servlet.http.HttpSession;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class userServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void findAll() {
        assertThat(userService.findAll()).isEqualTo(Collections.emptyList());
    }

//    @Test
//    void check() {
//        userService.checkRequestAboutMypage(new Request);
//    }
//    public void checkRequestAboutMypage(HttpSession httpSession) {
//        if (httpSession.getAttribute("user") == null) {
//            throw new UnacceptablePathException();
//        }
//    }
}
