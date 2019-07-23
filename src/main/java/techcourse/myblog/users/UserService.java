package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    public static final String EMAIL_DUPLICATE_MESSAGE = "이미 사용중인 이메일입니다.";
    public static final String PASSWORD_INVALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    private final UserRepository userRepository;

    public Long save(UserDto.Register userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        verifyPassword(userDto);
        User user = new User();

        BeanUtils.copyProperties(userDto, user);
        validateUser(user);

        return userRepository.save(user).getId();
    }

    private void verifyPassword(UserDto.Register userDto) {
        if (!userDto.isValidPassword()) {
            throw new ValidSingupException(PASSWORD_INVALID_MESSAGE, "password");
        }
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidSingupException(EMAIL_DUPLICATE_MESSAGE, "email");
        }
    }

    public UserDto.Response login(UserDto.Register userDto) {
        UserDto.Response userResponseDto = new UserDto.Response();
        User user = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(() -> new ValidSingupException("존재하지 않는 이메일 또는 비밀번호가 틀립니다.", "password"));

        validateUser(user);
        BeanUtils.copyProperties(user, userResponseDto);
        return userResponseDto;
    }

    public List<UserDto.Response> findAllExceptPassword() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    validateUser(user);
                    UserDto.Response responseDto = new UserDto.Response();
                    BeanUtils.copyProperties(user, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public UserDto.Response findById(Long id) {
        UserDto.Response userResponseDto = new UserDto.Response();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find User : " + id));

        validateUser(user);
        BeanUtils.copyProperties(user, userResponseDto);

        return userResponseDto;
    }

    public UserDto.Response update(Long id, UserDto.Update userDto) {
        UserDto.Response userResponseDto = new UserDto.Response();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find User : " + id));

        validateUser(user);
        user.setName(userDto.getName());
        BeanUtils.copyProperties(user, userResponseDto);

        return userResponseDto;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public void validateUser(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() != 0) {
            throw new ValidSingupException("Domain Error", "id");
        }
    }
}


