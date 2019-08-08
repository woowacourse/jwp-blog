package techcourse.myblog.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.exception.InvalidLoginFormException;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
public class LoginRestController {
    private final UserService userService;

    public LoginRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody @Valid UserLoginDto userDto, BindingResult result, HttpSession session) {
        log.debug(">>> userDto : {}", userDto);
        if (result.hasErrors()) {
            throw new InvalidLoginFormException(result.getFieldError().getDefaultMessage());
        }
        UserResponseDto user = userService.login(userDto);
        session.setAttribute("user", user);
        return user;
    }
}
