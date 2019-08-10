package techcourse.myblog.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.repository.ArticleRepository;
import techcourse.myblog.comment.service.CommentService;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.exception.DuplicateUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.repository.UserRepository;
import techcourse.myblog.utils.converter.UserConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserService {
    private static final String LOG_TAG = "[UserService]";
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public UserService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        checkRegisteredEmail(userRequestDto);
        User user = userRepository.save(UserConverter.toEntity(userRequestDto));

        return UserConverter.toResponseDto(user);
    }

    private void checkRegisteredEmail(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateUserException();
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public UserResponseDto updateUser(UserRequestDto userRequestDto, UserResponseDto origin) {
        User user = getUserByEmail(origin.getEmail());
        user.updateNameAndEmail(userRequestDto);
        return UserConverter.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);
    }

    public void deleteUser(UserResponseDto userResponseDto) {
        userRepository.delete(getUserByEmail(userResponseDto.getEmail()));
    }

    public void checkAuthentication(Long articleId, UserResponseDto userResponseDto) {
        User user = userRepository.findByEmail(userResponseDto.getEmail()).orElseThrow(NotFoundUserException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        log.debug("{} article.getAuthor().getEmail() >> {}", LOG_TAG, article.getAuthor().getEmail());

        if (!article.isAuthor(user)) {
            throw new NotFoundArticleException();
        }
    }
}