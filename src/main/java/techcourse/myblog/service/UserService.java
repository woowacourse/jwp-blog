package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.exception.UserException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(String email) {
        User user = userRepository.findUserByEmail(email);

        if (!email.equals(user.getEmail())) {
            throw new UserException("작성자가 아닙니다.");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserDto save(String sessionEmail, UserDto userDto) {
        if (!sessionEmail.equals(userDto.getEmail())) {
            throw new UserException("인증된 사용자 정보가 아닙니다.");
        }

        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.updateUser(userDto.getName(), userDto.getPassword());

        return userDto;
    }

    public User login(UserLoginDto loginDto) {
        User dbUser = userRepository.findUserByEmail(loginDto.getEmail());

        if (!dbUser.checkPassword(loginDto.getPassword())) {
            throw new UserException("패스워드가 올바르지 않습니다.");
        }

        return dbUser;
    }

    public void signUpUser(UserDto userDto) {
        if (!isNull(userDto.getEmail())) {
            throw new UserException("유저가 이미 존재합니다.");
        }

        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    public User findUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    private boolean isNull(String email) {
        return Objects.isNull(userRepository.findUserByEmail(email));
    }
}
