package techcourse.myblog.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    private final UserService userService;

    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> read(@PathVariable long userId) {
        UserDto resultDto = userService.findUser(userId);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto resultDto = userService.addUser(userDto);
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable long userId,
                                          @RequestBody UserDto userDto,
                                          @SessionInfo UserSessionInfo userSessionInfo) {
        userService.updateUser(userSessionInfo.toUser(), userDto);
        UserDto resultDto = userService.findUser(userId);
        return new ResponseEntity<>(resultDto, HttpStatus.ACCEPTED);
    }
}
