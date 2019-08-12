package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import static org.springframework.http.HttpMethod.GET;

class IndexControllerTests extends ControllerTestTemplate {
    @Test
    void index() {
        httpRequest(GET, "/").isOk();
    }
}