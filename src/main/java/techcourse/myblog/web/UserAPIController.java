package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.UserService;
import techcourse.myblog.application.dto.UserEditRequest;
import techcourse.myblog.web.dto.ErrorResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserAPIController {
    private static final String USER_INFO = "user";

    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable Long userId, HttpSession httpSession,
                                     @Valid @RequestBody UserEditRequest userEditRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
        }
        httpSession.setAttribute(USER_INFO, userService.update(userId, userEditRequest.getName()));

        return ResponseEntity.status(HttpStatus.OK)
            .header("Location", "/mypage")
            .build();

    }

}
