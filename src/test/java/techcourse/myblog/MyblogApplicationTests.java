package techcourse.myblog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyblogApplicationTests {

    @Test
    void contextLoads() {
    }

}
