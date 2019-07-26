package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTests {
    private final String validName = "yun";
    private final String validEmail = "hello@world.com";
    private final String validPassword = "123123123";

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유효성_검사() {
        User user = User.builder()
                .id(-1l)
                .name(validName)
                .email(validEmail)
                .password(validPassword)
                .build();

        userRepository.save(user);
    }

    @Test
    void 이름_유효성_검사_이름이_한글자인경우() {
        User user = User.builder()
                .id(-1l)
                .name("a")
                .email(validEmail)
                .password(validPassword)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 이름_유효성_검사_이름이_11글자인경우() {
        User user = User.builder()
                .id(-1l)
                .name("abcdefghijk")
                .email(validEmail)
                .password(validPassword)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 이름_유효성_검사_이름에_허용하지않는_글자를포함한경우() {
        User user = User.builder()
                .id(-1l)
                .name("1^")
                .email(validEmail)
                .password(validPassword)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 이메일_유효성_검사_중복() {
        User user = User.builder()
                .id(-1l)
                .name(validName)
                .email(validEmail)
                .password(validPassword)
                .build();

        userRepository.save(user);
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 이메일_유효성_검사_이메일_형식1() {
        User user = User.builder()
                .id(-1l)
                .name(validName)
                .email("email")
                .password(validPassword)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 이메일_유효성_검사_이메일_형식2() {
        User user = User.builder()
                .id(-1l)
                .name(validName)
                .email("email@daum.")
                .password(validPassword)
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void 비밀번호_유효성_검사_7자이하() {
        User user = User.builder()
                .id(-1l)
                .name(validName)
                .email(validEmail)
                .password("1234567")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }

    @Test
    //@Transactional
    void snsInfo_() {
        User user = User.builder()
                .id(1l)
                .name(validName)
                .email(validEmail)
                .password(validPassword)
                .build();

        SnsInfo snsInfo = SnsInfo.builder()
                .email(validEmail)
                //.user(user)
                .build();
        user.addSnsInfo(snsInfo);

        userRepository.save(user);

        //System.out.println(userRepository.findById(1L));
    }
}