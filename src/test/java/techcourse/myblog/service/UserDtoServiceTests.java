package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDtoServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_Test() {
        techcourse.myblog.UserDto userDto = techcourse.myblog.UserDto.builder().name("김강민")
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();
        userService.save(userDto);
        assertThat(userRepository.findById((long)1)).isNotNull();
    }

    @Test
    void uthenticate_Test(){
        techcourse.myblog.UserDto userDto = techcourse.myblog.UserDto.builder().name("김강민")
                .email("2@naver.com")
                .password("asdASD12!@")
                .build();
        userService.save(userDto);
        Optional<User> maybeUser = userService.authenticate("2@naver.com","asdASD12!@");
        assertThat(maybeUser.isPresent()).isTrue();
    }
}
