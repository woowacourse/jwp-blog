package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.domain.dto.UserDto;
import techcourse.myblog.domain.dto.UserUpdateRequestDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(UserDto userDto) {
        checkValidUserInformation(userDto);
        User user = userDto.toEntity();
        userRepository.save(user);
        log.info("새로운 {} 유저가 가입했습니다.", user.getUserName());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUser(String email, UserUpdateRequestDto userUpdateRequestDto) {
        User user = findByEmail(email);
        user.changeUserName(userUpdateRequestDto.getUserName());
    }

    public void deleteUser(String email) {
        User user = findByEmail(email);
        userRepository.delete(user);
    }

    private void checkValidUserInformation(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new NotValidUserInfoException("중복된 이메일 입니다.");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundObjectException::new);
    }
}
