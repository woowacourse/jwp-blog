package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.exception.UserArgumentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.service.exception.SignUpException;
import techcourse.myblog.service.exception.UserDeleteException;
import techcourse.myblog.service.exception.UserUpdateException;

import javax.transaction.Transactional;
import java.util.List;

import static techcourse.myblog.domain.exception.UserArgumentException.EMAIL_DUPLICATION_MESSAGE;
import static techcourse.myblog.domain.exception.UserArgumentException.PASSWORD_CONFIRM_FAIL_MESSAGE;

@Service
public class UserService {
    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    public UserService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserPublicInfoDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundUserException::new);
        return new UserPublicInfoDto(user.getId(), user.getName(), user.getEmail());
    }

    public void save(UserDto userDto) {
        try {
            validate(userDto);
            userRepository.save(userDto.toEntity());
        } catch (Exception e) {
            throw new SignUpException(e.getMessage());
        }
    }

    private void validate(UserDto userDto) {
        checkDuplicatedEmail(userDto.getEmail());
        checkPasswordConfirm(userDto);
    }

    private void checkDuplicatedEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserArgumentException(EMAIL_DUPLICATION_MESSAGE);
        }
    }

    private void checkPasswordConfirm(UserDto userDto) {
        if (!userDto.confirmPassword()) {
            throw new UserArgumentException(PASSWORD_CONFIRM_FAIL_MESSAGE);
        }
    }

    public void update(UserPublicInfoDto userPublicInfoDto) {
        try {
            User user = userRepository.findByEmail(userPublicInfoDto.getEmail())
                    .orElseThrow(NotFoundUserException::new);
            user.updateName(userPublicInfoDto.getName());
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserUpdateException(e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            articleRepository.deleteByUserId(id);
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new UserDeleteException(e.getMessage());
        }
    }
}
