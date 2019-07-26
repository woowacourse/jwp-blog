package techcourse.myblog.utils.converter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;

public class DtoConverter {
    public static User convert(UserRequestDto dto) {
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }
    public static Article convert(ArticleRequestDto dto) {
        return new Article(dto.getTitle(), dto.getContents(), dto.getCoverUrl());
    }
    public static UserResponseDto convert(User user) {
        return new UserResponseDto(user.getName(), user.getEmail());
    }
}