package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        checkDuplicatedEmail(userDto.getEmail());
        checkValidNameLength(userDto.getName());
        checkValidName(userDto.getName());
        userRepository.save(userDto.toEntity());
    }

    private void checkDuplicatedEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new SignUpException(SignUpException.EMAIL_DUPLICATION_MESSAGE);
        }
    }

    public void checkValidNameLength(String name) {
        int nameLength = name.length();
        if (nameLength < 2 || nameLength > 10) {
            throw new SignUpException(SignUpException.INVALID_NAME_LENGTH_MESSAGE);
        }
    }

    public void checkValidName(String name) {
        Pattern pattern = Pattern.compile("^[(가-힣a-zA-Z)]+$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.find()) {
            throw new SignUpException(SignUpException.NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
        }
    }

}
