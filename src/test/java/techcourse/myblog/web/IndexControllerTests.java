package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import techcourse.myblog.web.common.ControllerTestTemplate;

import static org.springframework.http.HttpMethod.GET;

class IndexControllerTests extends ControllerTestTemplate {
    @Test
    void index() {
        httpRequest(GET, "/").isOk();
    }
}