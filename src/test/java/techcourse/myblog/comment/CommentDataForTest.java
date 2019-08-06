package techcourse.myblog.comment;

import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

public class CommentDataForTest {
    public static final String COMMENT_CONTENTS = "쏘쏘 멋져";
    public static final String UPDATED_COMMENT_CONTENTS = "쏘쏘 더멋져";

    public static final BodyInserter<?, ? super ClientHttpRequest> NEW_COMMENT_BODY =
            BodyInserters
                    .fromFormData("contents", COMMENT_CONTENTS);

    public static final BodyInserter<?, ? super ClientHttpRequest> UPDATED_COMMENT_BODY =
            BodyInserters
                    .fromFormData("contents", UPDATED_COMMENT_CONTENTS);
}
