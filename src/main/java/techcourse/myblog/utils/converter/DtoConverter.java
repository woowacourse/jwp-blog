package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.SignUpRequest;
import techcourse.myblog.dto.UserResponse;

public class DtoConverter {

    public static User convert(SignUpRequest dto) {
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }

    public static Article convert(ArticleRequest dto, User author) {
        return new Article(dto.getTitle(), dto.getContents(), dto.getCoverUrl(), author);
    }

    public static UserResponse convert(User user) {
        return new UserResponse(user.getName(), user.getEmail());
    }
}