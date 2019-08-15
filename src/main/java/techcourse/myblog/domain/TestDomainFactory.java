package techcourse.myblog.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.utils.UserAssembler;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;

@Component
public class TestDomainFactory {
    private static final String COMMON_PASSWORD = "Password!1";

    @Autowired
    private UserAssembler userAssembler;

    private Long newUserId = 10l;
    private Long newArticleId = 200l;
    private Long newCommentId = 3000l;

    public User newUser() {
        String name = "userName";
        String email = String.format("test%d@test.com", newUserId);

        User user = userAssembler.toUserFromUserRequest(new UserRequest(name, email, COMMON_PASSWORD, COMMON_PASSWORD));
        user.setId(newUserId++);

        return user;
    }

    public Article newArticleWithoutAuthor() {
        String title = String.format("title%d", newArticleId);
        String coverUrl = "coverUrl";
        String contents = "contents";

        Article article = new Article(title, coverUrl, contents);
        article.setId(newArticleId++);

        return article;
    }

    public Comment newCommentWithArticleAndCommenter(Article article, User commenter) {
        String contents = String.format("contents%d", newCommentId);

        Comment comment = new Comment(contents, article, commenter);
        comment.setId(newCommentId++);

        return comment;
    }

    public UserRequest toUserRequestFromUser(User user) {
        UserRequest userRequest = new UserRequest();

        userRequest.setName(user.getName());
        userRequest.setEmail(user.getEmail());

        userRequest.setPassword(COMMON_PASSWORD);
        userRequest.setReconfirmPassword(COMMON_PASSWORD);

        return userRequest;
    }

    public UserLoginRequest toUserLoginRequestFromUser(User user) {
        UserLoginRequest userLoginRequest = new UserLoginRequest();

        userLoginRequest.setEmail(user.getEmail());
        userLoginRequest.setPassword(COMMON_PASSWORD);

        return userLoginRequest;
    }
}
