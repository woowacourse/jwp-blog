package techcourse.myblog.article;

import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

public class ArticleDataForTest {
    public static final String ARTICLE_TITLE = "article_title";
    public static final String ARTICLE_COVER_URL = "article_cover_url";
    public static final String ARTICLE_CONTENTS = "article_contents";

    public static final BodyInserter<?, ? super ClientHttpRequest> ARTICLE_BODY =
            BodyInserters
                    .fromFormData("title", ArticleDataForTest.ARTICLE_TITLE)
                    .with("coverUrl", ArticleDataForTest.ARTICLE_COVER_URL)
                    .with("contents", ArticleDataForTest.ARTICLE_CONTENTS);

    public static final String NEW_TITLE = "new_article_title";
    public static final String NEW_COVER_URL = "new_article_cover_url";
    public static final String NEW_CONTENTS = "new_article_contents";

    public static final BodyInserter<?, ? super ClientHttpRequest> NEW_ARTICLE_BODY =
            BodyInserters
                    .fromFormData("title", ArticleDataForTest.NEW_TITLE)
                    .with("coverUrl", ArticleDataForTest.NEW_COVER_URL)
                    .with("contents", ArticleDataForTest.NEW_CONTENTS);

    public static final String UPDATE_TITLE = "update_title";
    public static final String UPDATE_COVER_URL = "update_cover_url";
    public static final String UPDATE_CONTENTS = "update_contents";

    public static final BodyInserter<?, ? super ClientHttpRequest> UPDATE_BODY_INSERTER =
            BodyInserters
                    .fromFormData("title", ArticleDataForTest.UPDATE_TITLE)
                    .with("coverUrl", ArticleDataForTest.UPDATE_COVER_URL)
                    .with("contents", ArticleDataForTest.UPDATE_CONTENTS);
}
