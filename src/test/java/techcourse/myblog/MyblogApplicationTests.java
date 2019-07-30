package techcourse.myblog;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyblogApplicationTests {
    public final String COMMENT_CONTENTS = "contents123";
    public final long USER_ID = 1;
    public final String USER_NAME = "default";
    public final String USER_PASSWORD = "abcdEFGH123!@#";
    public final String USER_EMAIL = "default@default.com";
    public final long ARTICLE_ID = 1;
    public final String ARTICLE_TITLE = "title";
    public final String ARTICLE_CONTENTS = "contents";
    public final String ARTICLE_COVER_URL = "coverUrl";
    public final String USER_PASSWORD2 = "abcdEFGH123!@#";
    public final String USER_EMAIL2 = "default2@default.com";


}