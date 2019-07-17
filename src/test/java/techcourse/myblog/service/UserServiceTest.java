package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("email")
                .password("password")
                .name("name")
                .build();

        userService.save(modelMapper.map(user, UserDto.Create.class));
    }

    @Test
    void 회원정보_등록시_예외처리() {
        UserDto.Create duplicatedUser = modelMapper.map(user, UserDto.Create.class);
        assertThrows(DuplicatedUserException.class, () -> userService.save(duplicatedUser));
    }

    @Test
    void 회원정보_전체_조회_테스트() {
        List<UserDto.Response> users = userService.findAll();
        assertThat(users).isEqualTo(Arrays.asList(modelMapper.map(user, UserDto.Response.class)));
    }
}