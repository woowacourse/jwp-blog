package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static techcourse.myblog.service.exception.SignUpException.*;

@Service
public class UserService {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String KOREAN_ENGLISH_REGEX = "^[(ㄱ-ㅎ가-힣a-zA-Z)]+$";
    private static final String LOWER_CASE_REGEX = "[(a-z)]+";
    private static final String UPPER_CASE_REGEX = "[(A-Z)]+";
    private static final String NUMBER_REGEX = "[(0-9)]+";
    private static final String SPECIAL_CHARACTER_REGEX = "[ `~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+";
    private static final String KOREAN_REGEX = "[(ㄱ-ㅎ가-힣)]+";

    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    public UserService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public void save(UserDto userDto) {
        validate(userDto);
        userRepository.save(userDto.toEntity());
    }

    private void validate(UserDto userDto) {
        checkDuplicatedEmail(userDto.getEmail());
        checkValidNameLength(userDto.getName());
        checkValidName(userDto.getName());
        checkPasswordConfirm(userDto);
        checkValidPasswordLength(userDto.getPassword());
        checkValidPassword(userDto.getPassword());
    }

    private void checkDuplicatedEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new SignUpException(EMAIL_DUPLICATION_MESSAGE);
        }
    }

    private void checkValidNameLength(String name) {
        int nameLength = name.length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new SignUpException(INVALID_NAME_LENGTH_MESSAGE);
        }
    }

    private void checkValidName(String name) {
        if (!matchRegex(name, KOREAN_ENGLISH_REGEX)) {
            throw new SignUpException(NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
        }
    }

    private void checkPasswordConfirm(UserDto userDto) {
        if (!userDto.confirmPassword()) {
            throw new SignUpException(PASSWORD_CONFIRM_FAIL_MESSAGE);
        }
    }

    private void checkValidPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new SignUpException(INVALID_PASSWORD_LENGTH_MESSAGE);
        }
    }

    private void checkValidPassword(String password) {
        if (!matchRegex(password, LOWER_CASE_REGEX)
                || !matchRegex(password, UPPER_CASE_REGEX)
                || !matchRegex(password, NUMBER_REGEX)
                || !matchRegex(password, SPECIAL_CHARACTER_REGEX)
                || matchRegex(password, KOREAN_REGEX)) {
            throw new SignUpException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private boolean matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public void update(UserPublicInfoDto userPublicInfoDto) {
        User user = userRepository.findByEmail(userPublicInfoDto.getEmail());
        if (user == null) {
            throw new NotFoundUserException();
        }
        user.setName(userPublicInfoDto.getName());
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        articleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
