package techcourse.myblog.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateDto;
import techcourse.myblog.exception.CouldNotFindUserIdException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {
    public static final long TEST_USER_ID_NOT_IN_REPO = 2;

    public User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        user = userRepository.save(User.of(userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword())
        );
    }

    @Test
    @DisplayName("유저를 생성하고 저장한다.")
    void createTest() {
        UserDto userDto = new UserDto();
        userDto.setName("update");
        userDto.setEmail("update@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        User newCreateUser = userRepository.save(User.of(userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword())
        );

        assertThat(newCreateUser).isNotNull();
    }

    @Test
    @DisplayName("유저 id로 유저의 정보를 조회한다.")
    void findByIdTest() {
        Optional<User> findUser = userRepository.findById(user.getId());
        assertThat(findUser.isPresent()).isTrue();
        assertThat(findUser.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("유저의 email로 유저의 정보를 조회한다.")
    void findByEmailTest() {
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        assertTrue(findUser.isPresent());
        assertThat(findUser.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Repository에 없는 id를 조회하는 경우 예외를 던져준다.")
    void readFailTest() {
        assertThrows(CouldNotFindUserIdException.class, () ->
                userRepository.findById(TEST_USER_ID_NOT_IN_REPO)
                .orElseThrow(CouldNotFindUserIdException::new)
        );
    }

    @Test
    @DisplayName("유저 id를 이용해 해당 유저 정보를 업데이트 한다.")
    void updateByIdTest() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("kimhyojae");

        user.updateUser(userUpdateDto.getName());
        User updateUser = userRepository.save(user);

        assertThat(updateUser.getName()).isEqualTo("kimhyojae");
    }

    @Test
    @DisplayName("유저 id를 이용해 해당 유저의 정보를 지운다.")
    void deleteByIdTest() {
        userRepository.deleteById(user.getId());
        assertThrows(CouldNotFindUserIdException.class, () ->
                userRepository.findById(user.getId())
                .orElseThrow(CouldNotFindUserIdException::new)
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}