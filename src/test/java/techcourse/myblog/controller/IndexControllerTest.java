package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import techcourse.myblog.controller.test.WebClientGenerator;

import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class)
class IndexControllerTest extends WebClientGenerator {

    @Test
    void index() {
        responseSpec(GET, "/")
                .expectStatus()
                .isOk();
    }
}