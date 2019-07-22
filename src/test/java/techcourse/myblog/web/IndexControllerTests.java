package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class)
class IndexControllerTests extends WebClientGenerator {

    @Test
    void index() {
        response(GET, "/")
                .expectStatus()
                .isOk();
    }
}