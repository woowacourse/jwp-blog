package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import techcourse.myblog.web.test.WebClientGenerator;

import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class)
class IndexControllerTest extends WebClientGenerator {

    @Test
    void index() {
        response(GET, "/")
                .expectStatus()
                .isOk();
    }
}