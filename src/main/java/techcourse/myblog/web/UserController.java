package techcourse.myblog.web;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger LOG = getLogger(UserController.class);
    private static final String USER = "user";
    private static final String ERROR = "error";
    private static final String REDIRECT = "redirect:";
    private static final String ROUTE_ROOT = "/";
    private static final String ROUTE_SIGNUP = "/signup";
    private static final String ROUTE_USERS = "/users";
    private static final String ROUTE_LOGIN = "/login";
    private static final String ROUTE_LOGOUT = "/logout";
    private static final String ROUTE_MYPAGE = "/mypage";
    private static final String ROUTE_EDIT = "/edit";
    private static final String PAGE_LOGIN = "login";
    private static final String PAGE_USER_LIST = "user-list";
    private static final String PAGE_MYPAGE = "mypage";
    private static final String PAGE_MYPAGE_EDIT = "mypage-edit";

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    private String loginFirstOr(final String elsePage, final HttpSession session) {
        if (session.getAttribute(USER) == null) {
            return REDIRECT + ROUTE_LOGIN;
        }
        return elsePage;
    }

    @GetMapping(ROUTE_SIGNUP)
    public String signUpPage() {
        return "signup";
    }

    @GetMapping(ROUTE_USERS)
    public String userList(final Model model) {
        model.addAttribute("users", userService.findAll());
        return PAGE_USER_LIST;
    }

    @Transactional
    @PostMapping(ROUTE_USERS)
    public String signUp(final UserDto userDto) {
        userService.signUp(userDto);
        LOG.debug("회원 가입 성공");
        LOG.debug("username: {}", userDto.getUsername());
        LOG.debug("password: {}", userDto.getPassword());
        LOG.debug("email: {}", userDto.getEmail());
        return REDIRECT + PAGE_LOGIN;
    }

    @GetMapping(ROUTE_LOGIN)
    public String loginPage(final HttpSession session) {
        LOG.debug("user attr: {}", session.getAttribute(USER));
        if (session.getAttribute(USER) != null) {
            return REDIRECT + ROUTE_ROOT;
        }
        return PAGE_LOGIN;
    }

    @PostMapping(ROUTE_LOGIN)
    public String userLogin(final LoginDto loginDto, final HttpSession session) {
        LOG.debug("로그인 시도 시작");
//        try {
        LOG.debug("received email: {}", loginDto.getEmail());
        LOG.debug("received password: {}", loginDto.getPassword());
        final User existUser = userService.login(loginDto);
        session.setAttribute(USER, existUser);
        LOG.debug("로그인 성공!");
        LOG.debug("user: {}", existUser);
        return REDIRECT + ROUTE_ROOT;
    }

    @GetMapping(ROUTE_LOGOUT)
    public String userLogout(final HttpSession session) {
        LOG.debug("사용자 로그아웃");
        session.invalidate();
        return REDIRECT + ROUTE_ROOT;
    }

    @GetMapping(ROUTE_MYPAGE)
    public String myPage(final Model model, final HttpSession session) {
        final User user = (User) session.getAttribute(USER);
        model.addAttribute("user", user);
        return loginFirstOr(PAGE_MYPAGE, session);
    }

    @GetMapping(ROUTE_MYPAGE + ROUTE_EDIT)
    public String myPageEditor(final Model model, final HttpSession session) {
        final User user = (User) session.getAttribute(USER);
        model.addAttribute(USER, user);
        return loginFirstOr(PAGE_MYPAGE_EDIT, session);
    }

    @PutMapping(ROUTE_MYPAGE + ROUTE_EDIT)
    public String myPageEdit(final Model model, final HttpSession session, final UserDto dto) {
            final User loginUser = (User) session.getAttribute(USER);
            final Long userId = loginUser.getId();
            LOG.debug("사용자 변경");
            LOG.debug("user: {}", loginUser);
            LOG.debug("DTO: {}", dto.getUsername());
            final User updatedUser = userService.update(userId, dto);
            session.setAttribute(USER, updatedUser);
            return REDIRECT + ROUTE_MYPAGE;
    }

    @DeleteMapping(ROUTE_MYPAGE + ROUTE_EDIT)
    public String deleteUser(final HttpSession session) {
        try {
            final User loginUser = (User) session.getAttribute(USER);
            LOG.debug("사용자 계정 삭제");
            LOG.debug("삭제되는 계정: {}", loginUser.getEmail());
            userService.delete(loginUser);
            session.setAttribute(USER, null);
            return REDIRECT + ROUTE_LOGOUT;
        } catch (Exception e) {
            return REDIRECT + ROUTE_ROOT;
        }
    }
}
