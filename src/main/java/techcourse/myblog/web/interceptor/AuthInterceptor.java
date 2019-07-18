package techcourse.myblog.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final UserRepository userRepository;

    @Autowired
    public AuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getHeader("Authorization") == null) {
            return true;
        }

        String[] authSegments = request.getHeader("Authorization").split(" ");

        if (!authSegments[0].equals("Basic")) {
            return true;
        }

        // credential[0]: email
        // credential[1]: password
        String[] credential = new String(Base64Utils.decodeFromString(authSegments[1]), UTF_8).split(":", 2);

        userRepository.findByEmail(credential[0])
            .ifPresent(user -> {
                if (user.authentication(credential[0], credential[1])) {
                    request.getSession().setAttribute("user", user);
                }
            });

        return true;
    }
}
