package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    public static final String EMAIL_DUPLICATE_MESSAGE = "이미 사용중인 이메일입니다.";
    public static final String PASSWORD_INVALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    private final UserRepository userRepository;

    public Long save(UserDto userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        verifyPassword(userDto);
        User user = new User();

        BeanUtils.copyProperties(userDto, user);

        return userRepository.save(user).getId();
    }

    private void verifyPassword(UserDto userDto) {
        if (!userDto.isValidPassword()) {
            throw new ValidSingupException(PASSWORD_INVALID_MESSAGE, "password");
        }
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidSingupException(EMAIL_DUPLICATE_MESSAGE, "email");
        }
    }

    public UserResponseDto login(UserDto userDto) {
        UserResponseDto userResponseDto = new UserResponseDto();
        User user = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(() -> new ValidSingupException("존재하지 않는 이메일 또는 비밀번호가 틀립니다.", "password"));

        BeanUtils.copyProperties(user, userResponseDto);
        return userResponseDto;
    }

    public List<UserResponseDto> findAllExceptPassword() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserResponseDto responseDto = new UserResponseDto();
                    BeanUtils.copyProperties(user, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public UserResponseDto findById(Long id) {
        UserResponseDto userResponseDto = new UserResponseDto();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find User : " + id));

        BeanUtils.copyProperties(user, userResponseDto);

        return userResponseDto;
    }
}


