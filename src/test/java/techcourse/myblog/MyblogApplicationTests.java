package techcourse.myblog;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyblogApplicationTests {
    public final String COMMENT_CONTENTS = "contents123";
    public final long USER_ID = 1;
    public final String USER_NAME = "default";
    public final String USER_PASSWORD = "asdfASDF1234!@#$";
    public final String USER_EMAIL = "default@email.com";
    public final long ARTICLE_ID = 1;
    public final String ARTICLE_TITLE = "articleTitle";
    public final String ARTICLE_CONTENTS = "articleContents";
    public final String ARTICLE_COVER_URL = "articleCoverUrl";

}