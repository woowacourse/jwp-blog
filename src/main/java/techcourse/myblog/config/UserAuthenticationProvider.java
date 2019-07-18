package techcourse.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        Optional<User> maybeUser = userService.authenticate(email, password);

        if(!maybeUser.isPresent()){
            maybeUser.get().setPassword(null);
            throw new IllegalArgumentException("로그인 실패!");
        }

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(maybeUser.get(), null, authorities);
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}