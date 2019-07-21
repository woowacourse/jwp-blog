package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.NoSuchUserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String LOG_TAG= "[UserService]";
    private static final int NOT_FOUND_RESULT = 0;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDto userDto) {
        validateUniqueEmail(userDto);

        User user = UserAssembler.writeUser(userDto);
        userRepository.save(user);

        log.debug("{} User 저장 -> {}", LOG_TAG, user);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (userRepository.countByEmail(userDto.getEmail()) > NOT_FOUND_RESULT) {
            throw new DuplicateUserException();
        }
    }

    public List<UserDto> getAllUsers() {
        return UserAssembler.writeDtos(userRepository.findAll());
    }

    public User findUserByEmailAndPassword(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NoSuchUserException("잘못된 접근입니다.");
        }

        return user;
    }

    public User findUserByEmailAndPassword(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        checkUserByEmail(email);
        checkPassword(email, password);

        return userRepository.findUserByEmailAndPassword(email, password);
    }

    private void checkUserByEmail(String email) {
        if (userRepository.countByEmail(email) == NOT_FOUND_RESULT) {
            throw new FailedLoginException("존재하지 않는 이메일입니다.");
        }
    }

    private void checkPassword(String email, String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
            throw new FailedLoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateUser(UserDto userDto) {
        String updatedName = userDto.getName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            log.debug("{} {}", LOG_TAG, "존재하지 않는 회원의 정보는 수정할 수 없음");
            throw new NoSuchUserException();
        }
        userRepository.save(new User(user.getUserId(), updatedName, email, password));
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
