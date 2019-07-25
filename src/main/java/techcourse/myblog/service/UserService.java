package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.NoSuchUserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String LOG_TAG = "[UserService]";
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

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NoSuchUserException("잘못된 접근입니다."));
    }

    public User updateUser(UserDto userDto) {
        String updatedName = userDto.getName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userRepository.findUserByEmail(email) // 이 부분과
                .orElseThrow(() -> {
                    log.debug("{} {}", LOG_TAG, "존재하지 않는 회원의 정보는 수정할 수 없음");
                    return new NoSuchUserException();
                });

        return userRepository.save(new User(user.getUserId(), updatedName, email, password)); // 이 부분의
        // TODO 쿼리가 어떻게 날아가는지 확인해보기
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
