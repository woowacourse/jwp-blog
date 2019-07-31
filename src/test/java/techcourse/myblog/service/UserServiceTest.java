package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.userinfo.UserName;
import techcourse.myblog.dto.UserEditRequestDto;
import techcourse.myblog.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = userService.save(User.builder()
                .name("이름")
                .email("test@test.com")
                .password("password1!")
                .build());
    }

    @Test
    void save() {
        User newUser = User.builder()
                .name("이름")
                .email("test2@test.com")
                .password("password1!")
                .build();

        User savedUser = userService.save(newUser);

        assertThat(savedUser.getName()).isEqualTo(newUser.getName());
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(newUser.getPassword());

        userService.deleteUser(savedUser.getId());
    }

    @Test
    void findById() {
        Long id = user.getId();
        assertThat(userService.findById(id)).isEqualTo(user);
    }

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        for (User foundUser : userService.findAll()) {
            users.add(foundUser);
        }

        assertThat(users.contains(user)).isTrue();
    }

    @Test
    void update() {
        UserEditRequestDto userEditRequestDto = new UserEditRequestDto();
        userEditRequestDto.setName("새이름");
        Long id = user.getId();

        userService.update(id, userEditRequestDto);

        User updatedUser = userService.findById(id);
        UserName updatedUserName = updatedUser.getName();

        assertThat(updatedUserName.getName()).isEqualTo(userEditRequestDto.getName());
    }

    @Test
    void deleteUser() {
        User newUser = User.builder()
                .name("이름")
                .email("test2@test.com")
                .password("password1!")
                .build();

        User savedUser = userService.save(newUser);
        Long id = savedUser.getId();

        userService.deleteUser(id);

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(user.getId());
    }
}