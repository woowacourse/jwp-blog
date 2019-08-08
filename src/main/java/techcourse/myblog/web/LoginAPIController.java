package techcourse.myblog.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.application.UserService;
import techcourse.myblog.web.dto.BaseResponse;
import techcourse.myblog.web.dto.ErrorResponse;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.exception.JsonAPIException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class LoginAPIController {
    private static final String USER_INFO = "user";

    private final UserService userService;

    public LoginAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult,
                                              HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
        }

        try {
            httpSession.setAttribute(USER_INFO, userService.checkLogin(loginRequest));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new JsonAPIException(e.getMessage());
        }

    }
}
