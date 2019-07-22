package techcourse.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.WebRequest;
import techcourse.myblog.exception.NotValidException;

import java.util.Map;

@SpringBootApplication
public class MyblogApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }

    // https://jojoldu.tistory.com/129
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final boolean includeStackTrace) {
                final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
                final Throwable error = getError(webRequest);
                if (error instanceof NotValidException) {
                    errorAttributes.put("errors", ((NotValidException) error).getErrors());
                }
                return errorAttributes;
            }
        };
    }

}
