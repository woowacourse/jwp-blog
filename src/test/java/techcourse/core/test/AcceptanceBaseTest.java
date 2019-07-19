package techcourse.core.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceBaseTest {
    @Value("${local.server.port}")
    private int port;

    protected WebTestClient anonymousClient() {
        return getDefaultClientBuilder()
                .build();
    }

    protected WebTestClient normalUserClient(String userId, String password) {
        return getDefaultClientBuilder()
                .filter(basicAuthentication(userId, password))
                .build();
    }

    private WebTestClient.Builder getDefaultClientBuilder() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port);
    }
}
