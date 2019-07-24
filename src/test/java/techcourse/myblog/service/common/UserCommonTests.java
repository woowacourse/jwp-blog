package techcourse.myblog.service.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;
import techcourse.myblog.service.dto.UserDto;

@SpringBootTest
public class UserCommonTests {
    @Autowired
    protected UserWriteService userWriteService;
    @Autowired
    protected UserReadService userReadService;
    protected UserDto userDto;
    protected User user;
    
    @BeforeEach
    void 유저_등록() {
        userDto = new UserDto("ab", "abcd@abcd", "12345678!Aa");
        user = userWriteService.save(userDto);
    }
    
    @AfterEach()
    void 유저_삭제() {
        userWriteService.deleteById(user.getId());
    }
}
