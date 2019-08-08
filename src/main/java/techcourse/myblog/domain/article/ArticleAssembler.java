package techcourse.myblog.domain.article;

import techcourse.myblog.domain.user.User;

public class ArticleAssembler {
    public static Article toEntity(Contents contents, User user) {
        return new Article(contents,user);
    }
}
