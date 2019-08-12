package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.NotFoundCommentException;
import techcourse.myblog.service.exception.SignUpException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.Utils.TestConstants.VALID_PASSWORD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("이메일이 중복되는 경우에 예외를 던져준다.")
    void checkEmailDuplication() {
        UserDto userDto1 = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        UserDto userDto2 = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);

        userService.save(userDto1);

        assertThatThrownBy(() -> userService.save(userDto2))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 2자 미만인 경우에 예외를 던져준다.")
    void underValidNameLength() {
        UserDto userDto = new UserDto("a", "email1@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 10자 초과인 경우에 예외를 던져준다.")
    void exceedValidNameLength() {
        UserDto userDto = new UserDto("abcdefghijk", "email2@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 숫자를 포함하는 경우에 예외를 던져준다.")
    void includeNumberInName() {
        UserDto userDto = new UserDto("abcde1", "email3@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("이름이 특수문자를 포함하는 경우에 예외를 던져준다.")
    void includeSpecialCharacterInName() {
        UserDto userDto = new UserDto("abcde!@", "email4@woowa.com",
                VALID_PASSWORD, VALID_PASSWORD);

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password가 8자 미만인 경우 예외를 던져준다.")
    void underValidPasswordLength() {
        UserDto userDto = new UserDto("abcde", "email5@woowa.com",
                "passwor", "passwor");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 소문자가 포함되지 않으면 예외를 던져준다.")
    void checkUndercaseInPassword() {
        UserDto userDto = new UserDto("abcde", "email6@woowa.com",
                "PASSWORD1!", "PASSWORD1!");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 대문자가 포함되지 않으면 예외를 던져준다.")
    void checkUppercaseInPassword() {
        UserDto userDto = new UserDto("abcde", "email7@woowa.com",
                "password1!", "password1!");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 숫자가 포함되지 않으면 예외를 던져준다.")
    void checkNumberInPassword() {
        UserDto userDto = new UserDto("abcde", "email8@woowa.com",
                "passWORD!", "passWORD!");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 특수문자가 포함되지 않으면 예외를 던져준다.")
    void checkSpecialCharacterInPassword() {
        UserDto userDto = new UserDto("abcde", "email9@woowa.com",
                "passWORD1", "passWORD1");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("password에 한글이 포함되면 예외를 던져준다.")
    void checkPasswordDoesNotContainsKorean() {
        UserDto userDto = new UserDto("abcde", "email10@woowa.com",
                "passWORD가1!", "passWORD가1!");

        assertThatThrownBy(() -> userService.save(userDto))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    @DisplayName("User를 삭제했을 때 작성한 Article도 삭제된다")
    void deleteUserWithCascadeArticles() {
        UserDto userDto = new UserDto("delete", "email11@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User author = userService.save(userDto);
        ArticleDto articleDto = new ArticleDto(null, author.getId(), "title",
                "coverUrl", "contents");

        ArticleDto article = articleService.save(author.getId(), articleDto);
        userService.delete(author.getId(), author.getId());

        assertThatThrownBy(() -> articleService.findArticleDtoById(article.getId()))
                .isInstanceOf(NotFoundArticleException.class);
    }

    @Test
    @DisplayName("User를 삭제했을 때 작성한 Comment도 삭제된다")
    void deleteUserWithCascadeComments() {
        UserDto userDto = new UserDto("delete", "email11@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        User author = userService.save(userDto);

        ArticleDto articleDto = new ArticleDto(null, author.getId(),
                "title", "coverUrl", "contents");
        ArticleDto article = articleService.save(author.getId(), articleDto);

        CommentRequestDto commentRequestDto = new CommentRequestDto(article.getId(), "TEST Comment");
        Comment comment = commentService.save(author.getId(), commentRequestDto);

        userService.delete(author.getId(), author.getId());

        assertThatThrownBy(() -> commentService.findById(comment.getId()))
                .isInstanceOf(NotFoundCommentException.class);
    }
}