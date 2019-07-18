package techcourse.myblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/article/writing","/auth/**").hasAnyRole( "USER")
                .antMatchers("/css/**", "/js/**", "/img/**","/article/*","/","/user/signup","/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user") // default
                .loginProcessingUrl("/authenticate")
                .failureUrl("/user?error") // default
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.logout()
                .logoutUrl("/logout") // default
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
