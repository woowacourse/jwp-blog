package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.*;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.NotFoundCommentException;
import techcourse.myblog.service.exception.SignUpException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    public static final String VALID_PASSWORD = "passWORD1!";
    private static final Long UPDATE_ID = 2L;

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("이메일이 중복되는 경우에 예외를 던져준다.")
    public void checkEmailDuplication() {
        UserRequestSignUpDto userRequestSignUpDto1
                = new UserRequestSignUpDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        UserRequestSignUpDto userRequestSignUpDto2
                = new UserRequestSignUpDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);

        userService.save(userRequestSignUpDto1);

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto2))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 2자 미만인 경우에 예외를 던져준다.")
    public void underValidNameLength() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("a", "email1@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 10자 초과인 경우에 예외를 던져준다.")
    public void exceedValidNameLength() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcdefghijk", "email2@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 숫자를 포함하는 경우에 예외를 던져준다.")
    public void includeNumberInName() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde1", "email3@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 특수문자를 포함하는 경우에 예외를 던져준다.")
    public void includeSpecialCharacterInName() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde!@", "email4@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password가 8자 미만인 경우 예외를 던져준다.")
    public void underValidPasswordLength() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email5@woowa.com",
                "passwor", "passwor");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 소문자가 포함되지 않으면 예외를 던져준다.")
    public void checkUndercaseInPassword() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email6@woowa.com",
                "PASSWORD1!", "PASSWORD1!");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 대문자가 포함되지 않으면 예외를 던져준다.")
    public void checkUppercaseInPassword() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email7@woowa.com",
                "password1!", "password1!");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 숫자가 포함되지 않으면 예외를 던져준다.")
    public void checkNumberInPassword() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email8@woowa.com",
                "passWORD!", "passWORD!");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 특수문자가 포함되지 않으면 예외를 던져준다.")
    public void checkSpecialCharacterInPassword() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email9@woowa.com",
                "passWORD1", "passWORD1");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 한글이 포함되면 예외를 던져준다.")
    public void checkPasswordDoesNotContainsKorean() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("abcde", "email10@woowa.com",
                "passWORD가1!", "passWORD가1!");

        assertThatThrownBy(() -> userService.save(userRequestSignUpDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("User를 삭제했을 때 작성한 Article도 삭제된다")
    public void deleteUserWithCascadeArticles() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("delete", "email11@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User author = userService.save(userRequestSignUpDto);
        UserSessionDto userPublicInfoDto = new UserSessionDto(author.getId(), author.getName(), author.getEmail());
        ArticleDto articleDto = new ArticleDto(null, author.getId(), "title",
                "coverUrl", "contents");

        ArticleDto article = articleService.save(userPublicInfoDto, articleDto);
        userService.delete(author.getId());

        assertThatThrownBy(() -> articleService.findArticleDtoById(article.getId()))
                .isInstanceOf(NotFoundArticleException.class);
    }

    @Test
    @DisplayName("User를 삭제했을 때 작성한 Comment도 삭제된다")
    public void deleteUserWithCascadeComments() {
        UserRequestSignUpDto userRequestSignUpDto = new UserRequestSignUpDto("delete", "email11@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User author = userService.save(userRequestSignUpDto);
        UserSessionDto userPublicInfoDto = new UserSessionDto(author.getId(), author.getName(), author.getEmail());
        ArticleDto articleDto = new ArticleDto(null, author.getId(),
                "title", "coverUrl", "contents");
        ArticleDto article = articleService.save(userPublicInfoDto, articleDto);

        UserSessionDto userSession = new UserSessionDto(author.getId(), null, null);
        CommentRequestDto commentRequestDto = new CommentRequestDto(article.getId(), "TEST Comment");
        Comment comment = commentService.save(userSession, commentRequestDto);

        userService.delete(author.getId());

        assertThatThrownBy(() -> commentService.findById(comment.getId()))
                .isInstanceOf(NotFoundCommentException.class);
    }

    @Test
    @DisplayName("유저 이름 변경")
    void update() {
        Long userId = UPDATE_ID;
        User user = userService.findById(userId);
        String updateName = "UPDATE";
        UserRequestUpdateDto userRequestUpdateDto =
                new UserRequestUpdateDto(updateName, user.getEmail());
        userService.update(userId, userRequestUpdateDto);
        User updateUser = userService.findById(userId);

        assertThat(updateUser.getName()).isEqualTo(updateName);
    }
}