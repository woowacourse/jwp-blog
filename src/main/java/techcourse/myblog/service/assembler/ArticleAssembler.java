package techcourse.myblog.service.assembler;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.Contents;
import techcourse.myblog.domain.user.User;

public class ArticleAssembler {
    public static Article toEntity(Contents contents, User user) {
        return new Article(contents,user);
    }
}
