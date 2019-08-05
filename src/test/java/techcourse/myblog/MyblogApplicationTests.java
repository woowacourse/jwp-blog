package techcourse.myblog;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyblogApplicationTests {
    protected final String COMMENT_CONTENTS = "contents123";
    protected final long COMMENT_ID = 1;
    protected final long DEFAULT_USER_ID = 1;
    protected final String DEFAULT_USER_NAME = "default";
    protected final String DEFAULT_USER_PASSWORD = "abcdEFGH123!@#";
    protected final String DEFAULT_USER_EMAIL = "default@default.com";
    protected final long TEST_USER_ID = 2;
    protected final String TEST_USER_NAME = "default2";
    protected final String TEST_USER_PASSWORD = "abcdEFGH123!@#";
    protected final String TEST_USER_EMAIL = "default2@default.com";
    protected final long ARTICLE_ID = 1;
    protected final String ARTICLE_TITLE = "목적의식 있는 연습을 통한 효과적인 학습2";
    protected final String ARTICLE_CONTENTS = "2나는 우아한형제들에서 우아한테크코스 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 \"선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?\"였다.";
    protected final String ARTICLE_COVER_URL = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/5tdm/image/7OdaODfUPkDqDYIQKXk_ET3pfKo2.jpeg";
    protected final String ARTICLE_UNI_CONTENTS = StringEscapeUtils.escapeJava(ARTICLE_CONTENTS);
    @Test
    void contextLoads() {
    }

}
