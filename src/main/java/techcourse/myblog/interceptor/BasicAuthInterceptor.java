package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.web.UserService;
import techcourse.myblog.web.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

@Component
public class BasicAuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthInterceptor.class);

    private UserService userService;

    public BasicAuthInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic")) {
            return true;
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        String email = values[0];
        String password = values[1];

        log.debug("username : {}", email);
        log.debug("password : {}", password);

        //TODO 이렇게 만들어져있는 기본생성자와 setter를 통해 객체를 생성하는게 좋을까?
        //아니면 생성자를 만들어주거나 builder 패턴을 활용하는건 어떨까?
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(email);
        userLoginDto.setPassword(password);

        User user = userService.login(userLoginDto);
        UserSession userSession = new UserSession(user);
        request.getSession().setAttribute("user", userSession);
        return true;
    }
}
