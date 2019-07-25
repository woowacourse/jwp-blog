package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.exception.UserException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String NAME_PATTERN = "^([a-zA-Z]){2,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$";

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
    public String saveMyPage(String sessionEmail, UserDto userDto) {
        if (!sessionEmail.equals(userDto.getEmail())) {
            throw new UserException("인증된 사용자 정보가 아닙니다.");
        }

        if (!(validateName(userDto.getName()) && validatePassword(userDto.getPassword()))) {
            throw new UserException("아이디와 비밀번호가 올바르지 않습니다.");
        }

        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.updateUser(userDto.getName(), userDto.getPassword());

        userRepository.save(user);
        return user.getName();
    }

    public User login(UserLoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail());

        if (Objects.isNull(user)) {
            throw new UserException("아이디가 올바르지 않습니다.");
        }
        if (!user.checkPassword(loginDto.getPassword())) {
            throw new UserException("패스워드가 올바르지 않습니다.");
        }

        return user;
    }

    public void signUpUser(UserDto userDto) {
        if (!validateUser(userDto)) {
            throw new UserException("유저가 이미 존재합니다.");
        }

        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    public User findUser(String email) {
        User user = userRepository.findUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UserException("유저를 찾지 못했습니다.");
        }

        return user;
    }

    private boolean validateUser(UserDto userDto) {
        return validateName(userDto.getName())
                && validatePassword(userDto.getPassword())
                && validateEmail(userDto.getEmail());
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean validateEmail(String email) {
        return Objects.isNull(userRepository.findUserByEmail(email));
    }
}
