package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTests extends MyblogApplicationTests {
    UserDto userDto;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().name("김강민")
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();

        userService.save(userDto);
    }

    @Test
    @DisplayName("save가 잘 되었는지 테스트")
    void save_Test() {
        assertThat(userRepository.findById((long) 1)).isNotNull();
    }

    @Test
    @DisplayName("패스워드가 일치할 떄 true를 반환하는지 테스트")
    void checkPassword_Match_Test() {
        UserDto newUserDto = UserDto.builder().name("김강민")
                .email("kangmin789@naver.com")
                .password("asdASD12!@")
                .build();
        assertThat(userService.checkPassword(newUserDto)).isTrue();
    }

    @Test
    @DisplayName("패스워드가 불일치할 떄 익셉션을 던지는 지 테스트")
    void checkPassword_isNotMatch_Test() {
        UserDto newUserDto = UserDto.builder().name("김강민")
                .email("kangmin789@naver.com")
                .password("asdASD12")
                .build();
        assertThrows(IllegalArgumentException.class, () -> userService.checkPassword(newUserDto));
    }

    @Test
    @DisplayName("이메일이 존재할 떄 해당 이메일을 잘 가져오는 지 테스트")
    void getUserDtoByEmail_isPresent_Test() {
        assertThat(userService.getUserDtoByEmail(userDto.getEmail()).isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일이 존재하지 않을 때 해당 이메일을 잘 가져오지 않는 지 테스트")
    void getUserDtoByEmail_isEmpty_Test() {
        assertThat(!userService.getUserDtoByEmail("abc@naver.com").isPresent()).isTrue();
    }

    @Test
    @DisplayName("이름을 바꿀때 잘 바뀌는 지 테스트")
    void updateUserName_test() {
        userDto.setName("로비");
        assertThat(userService.updateUserName(userDto)).isEqualTo("로비");
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(userDto);
    }
}
