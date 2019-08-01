package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.AlreadyExistUserException;
import techcourse.myblog.exception.UserForbiddenException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.translator.ModelTranslator;
import techcourse.myblog.translator.UserTranslator;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ModelTranslator<User, UserDto> userTranslator;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userTranslator = new UserTranslator();
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public void register(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());

        if (maybeUser.isPresent()) {
            throw new AlreadyExistUserException("이미 존재하는 이메일입니다.");
        }

        User user = userTranslator.toEntity(new User(), userDto);
        userRepository.save(user);
    }

    public UserDto update(UserDto userDto, String email, String sessionEmail) {
        User authenticatedUser = getAuthenticatedUser(email, sessionEmail);
        User user = userTranslator.toEntity(authenticatedUser, userDto);

        return userTranslator.toDto(userRepository.save(user), new UserDto());
    }

    public void exit(String email, String sessionEmail) {
        User authenticatedUser = getAuthenticatedUser(email, sessionEmail);

        userRepository.delete(authenticatedUser);
    }

    @Transactional(readOnly = true)
    User getAuthenticatedUser(String email, String sessionEmail) {
        if (sessionEmail == null) {
            throw new UserForbiddenException("로그인이 필요합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다."));

        userValidateBy(user, sessionEmail);
        return user;
    }

    @Transactional(readOnly = true)
    public UserDto getUserInfo(String email, String sessionEmail) {
        if (sessionEmail == null) {
            throw new UserForbiddenException("로그인이 필요합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다."));

        userValidateBy(user, sessionEmail);
        return userTranslator.toDto(userRepository.save(user), new UserDto());
    }

    private void userValidateBy(User user, String sessionEmail) {
        if (!user.equals(sessionEmail)) {
            throw new UserForbiddenException("인증되지 않은 사용자입니다.");
        }
    }
}
