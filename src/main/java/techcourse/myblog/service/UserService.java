package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String KOREAN_ENGLISH_REGEX = "^[(ㄱ-ㅎ가-힣a-zA-Z)]+$";
    private static final String UNDER_CASE_REGEX = "[(a-z)]+";
    private static final String UPPER_CASE_REGEX = "[(A-Z)]+";
    private static final String NUMBER_REGEX = "[(0-9)]+";
    private static final String SPECIAL_CHARACTER_REGEX = "[ `~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+";
    private static final String KOREAN_REGEX = "[(ㄱ-ㅎ가-힣)]+";

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        checkDuplicatedEmail(userDto.getEmail());
        checkValidNameLength(userDto.getName());
        checkValidName(userDto.getName());
        checkValidPasswordLength(userDto.getPassword());
        checkValidPassword(userDto.getPassword());
        userRepository.save(userDto.toEntity());
    }

    private void checkDuplicatedEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new SignUpException(SignUpException.EMAIL_DUPLICATION_MESSAGE);
        }
    }

    private void checkValidNameLength(String name) {
        int nameLength = name.length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new SignUpException(SignUpException.INVALID_NAME_LENGTH_MESSAGE);
        }
    }

    private void checkValidName(String name) {
        if (!matchRegex(name, KOREAN_ENGLISH_REGEX)) {
            throw new SignUpException(SignUpException.NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
        }
    }

    private void checkValidPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new SignUpException(SignUpException.INVALID_PASSWORD_LENGTH_MESSAGE);
        }
    }

    private void checkValidPassword(String password) {
        if (!matchRegex(password, UNDER_CASE_REGEX)
                || !matchRegex(password, UPPER_CASE_REGEX)
                || !matchRegex(password, NUMBER_REGEX)
                || !matchRegex(password, SPECIAL_CHARACTER_REGEX)
                || matchRegex(password, KOREAN_REGEX)) {
            throw new SignUpException(SignUpException.INVALID_PASSWORD_MESSAGE);
        }
    }

    private boolean matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
