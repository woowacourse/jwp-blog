package techcourse.myblog.utils;

import techcourse.myblog.application.dto.ArticleDto;

public class ArticleTestObjects {
    public static final ArticleDto ARTICLE_DTO = new ArticleDto("title", "coverUrl", "contents");
    public static final ArticleDto UPDATE_ARTICLE_DTO = new ArticleDto("new-title", "new-coverUrl", "new-contents");
}
