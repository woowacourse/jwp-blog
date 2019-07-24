package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserRequestDto;

public class DtoConverter {
    public static User convert(UserRequestDto dto) {
        return new User(dto.getName(), dto.getPassword(), dto.getEmail());
    }

    public static Article convert(ArticleRequestDto dto) {
        return new Article(dto.getTitle(), dto.getContents(), dto.getCoverUrl());
    }
}
