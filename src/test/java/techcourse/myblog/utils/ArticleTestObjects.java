package techcourse.myblog.utils;

import techcourse.myblog.domain.article.ArticleFeature;

public class ArticleTestObjects {
    public static final ArticleFeature ARTICLE_FEATURE = new ArticleFeature("title", "coverUrl", "contents");
    public static final ArticleFeature UPDATE_ARTICLE_FEATURE = new ArticleFeature("new-title", "new-coverUrl", "new-contents");
}
