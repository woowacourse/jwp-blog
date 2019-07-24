package techcourse.myblog.domain;

import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.exception.UserArgumentException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static techcourse.myblog.service.exception.UserArgumentException.*;

public class UserFactory {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String KOREAN_ENGLISH_REGEX = "^[(ㄱ-ㅎ가-힣a-zA-Z)]+$";
    private static final String LOWER_CASE_REGEX = "[(a-z)]+";
    private static final String UPPER_CASE_REGEX = "[(A-Z)]+";
    private static final String NUMBER_REGEX = "[(0-9)]+";
    private static final String SPECIAL_CHARACTER_REGEX = "[ `~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+";
    private static final String KOREAN_REGEX = "[(ㄱ-ㅎ가-힣)]+";

    private UserFactory() {}

    public static User generateUser(UserDto userDto) {
        validate(userDto);
        return new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }

    private static void validate(UserDto userDto) {
        checkValidNameLength(userDto.getName());
        checkValidName(userDto.getName());
        checkPasswordConfirm(userDto);
        checkValidPasswordLength(userDto.getPassword());
        checkValidPassword(userDto.getPassword());
    }

    public static void checkValidNameLength(String name) {
        int nameLength = name.length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new UserArgumentException(INVALID_NAME_LENGTH_MESSAGE);
        }
    }

    public static void checkValidName(String name) {
        if (!matchRegex(name, KOREAN_ENGLISH_REGEX)) {
            throw new UserArgumentException(NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
        }
    }

    private static void checkPasswordConfirm(UserDto userDto) {
        if (!userDto.confirmPassword()) {
            throw new UserArgumentException(PASSWORD_CONFIRM_FAIL_MESSAGE);
        }
    }

    private static void checkValidPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new UserArgumentException(INVALID_PASSWORD_LENGTH_MESSAGE);
        }
    }

    private static void checkValidPassword(String password) {
        if (!matchRegex(password, LOWER_CASE_REGEX)
                || !matchRegex(password, UPPER_CASE_REGEX)
                || !matchRegex(password, NUMBER_REGEX)
                || !matchRegex(password, SPECIAL_CHARACTER_REGEX)
                || matchRegex(password, KOREAN_REGEX)) {
            throw new UserArgumentException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private static boolean matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
