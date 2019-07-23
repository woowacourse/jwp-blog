package techcourse.myblog.web;

import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpMethod.GET;

class IndexControllerTests extends ControllerTestTemplate {
    @Test
    void index() {
        requestExpect(GET, "/").isOk();
    }
}