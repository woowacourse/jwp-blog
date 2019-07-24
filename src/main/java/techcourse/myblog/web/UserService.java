package techcourse.myblog.web;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;

import java.util.List;

@Service
public class UserService {
    private static final String INVALID_ACCESS = "잘못된 접근입니다.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(UserLoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail()).orElseThrow(() -> new UserException("아이디가 올바르지 않습니다."));
        if (!user.checkPassword(loginDto.getPassword())) {
            throw new UserException("올바르지 않은 비밀번호 입니다.");
        }

        return user;
    }

    public User save(UserDto userDto) {
        User user = UserAssembler.writeUser(userDto);
        userRepository.save(user);
        return user;
    }

    public User update(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail()).orElseThrow(() -> new UserException(INVALID_ACCESS));
        user.update(userDto);
        userRepository.save(user);
        //TODO 여기서 DTO를 던져주는게 더 좋지않을까?
        return user;
    }

    public void delete(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new UserException(INVALID_ACCESS));
        userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserException(INVALID_ACCESS));
    }
}
