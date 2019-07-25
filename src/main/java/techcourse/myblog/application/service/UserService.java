package techcourse.myblog.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.error.DuplicatedEmailException;
import techcourse.myblog.error.WrongPasswordException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    public final static String DUPLICATE_EMAIL_MESSAGE = "중복된 이메일 입니다.";
    public final static String WRONG_PASSWORD_MESSAGE = "비밀번호가 틀렸습니다.";

    private final UserRepository userRepository;

    @Transactional
    public User save(UserDto userDto) {
        emailDuplicateValidate(userDto);
        log.debug("UserService.save() : " + userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .build();
        return userRepository.save(user);
    }

    private void emailDuplicateValidate(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicatedEmailException(DUPLICATE_EMAIL_MESSAGE);
        }
    }

    public boolean checkPassword(LoginDto loginDto) {
        Optional<User> maybeUser = userRepository.findByEmail(loginDto.getEmail());
        if (maybeUser.isPresent() && maybeUser.get().isSamePassword(loginDto.getPassword())) {
            return true;
        }
        throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE);
    }

    public Optional<User> getUserByEmail(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if (maybeUser.isPresent()) {
            return Optional.of(maybeUser.get());
        }
        return Optional.empty();
    }

    @Transactional
    public User updateUserName(User user, String name) {
        User targetUser = userRepository.findByEmail(user.getEmail()).get();
        targetUser.updateName(name);
        userRepository.save(targetUser);
        return targetUser;
    }

    public void deleteUser(User user) {
        User targetUser = userRepository.findByEmail(user.getEmail()).get();
        userRepository.delete(targetUser);
        log.debug("delete User : " + targetUser.toString());
    }
}
