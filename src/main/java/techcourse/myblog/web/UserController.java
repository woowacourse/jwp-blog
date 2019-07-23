package techcourse.myblog.web;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.MyPageEditDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.NoSuchElementException;

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
    private static final String DUPLICATED_EMAIL = "이미 가입되어 있는 이메일 주소입니다. 다른 이메일 주소를 입력해 주세요.";
    private static final String WRONG_LOGIN = "잘못된 이메일 주소 또는 패스워드를 입력하셨습니다.";

    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isDuplicatedEmail(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private String loginFirstOr(final String elsePage, final HttpSession session) {
        if (session.getAttribute(USER) == null) {
            return REDIRECT + ROUTE_LOGIN;
        }
        return elsePage;
    }

    @GetMapping(ROUTE_SIGNUP)
    public String singupPage() {
        return "signup";
    }

    @GetMapping(ROUTE_USERS)
    public String userList(final Model model) {
        model.addAttribute("users", userRepository.findAll());
        return PAGE_USER_LIST;
    }

    @Transactional
    @PostMapping(ROUTE_USERS)
    public String signup(final Model model, @Valid UserDto userDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final FieldError error = bindingResult.getFieldError();
            LOG.debug("클라이언트에서 전송된 필드에 오류 있음: {}", error.getField());
            model.addAttribute(ERROR, error.getField());
            return ROUTE_SIGNUP;
        }
        if (isDuplicatedEmail(userDto.getEmail())) {
            LOG.debug("중복 이메일 계정: {}", userDto.getEmail());
            model.addAttribute(ERROR, DUPLICATED_EMAIL);
            return ROUTE_SIGNUP;
        }
        final User user = new User(userDto);
        userRepository.save(user);
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
    public String userLogin(final Model model, final LoginDto loginDto, final HttpSession session) {
        LOG.debug("로그인 시도 시작");
        try {
            final User existUser = userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword()).get();
            session.setAttribute(USER, existUser);
            LOG.debug("로그인 성공!");
            return REDIRECT + ROUTE_ROOT;
        } catch (NoSuchElementException e) {
            LOG.debug("이메일 주소 또는 패스워드 오류: {}", e.getMessage());
            model.addAttribute(ERROR, WRONG_LOGIN);
            return ROUTE_LOGIN;
        } catch (Exception e) {
            LOG.debug("로그인 오류: {}", e.getMessage());
            model.addAttribute(ERROR, e.getMessage());
            return ROUTE_LOGIN;
        }
    }

    @GetMapping(ROUTE_LOGOUT)
    public String userLogout(final HttpSession session) {
        LOG.debug("사용자 로그아웃");
        session.setAttribute(USER, null);
        return REDIRECT + ROUTE_ROOT;
    }

    @GetMapping(ROUTE_MYPAGE)
    public String myPage(final Model model, final HttpSession session) {
        final User user = (User) session.getAttribute(USER);
        model.addAttribute("user", user);
        return loginFirstOr(PAGE_MYPAGE, session);
    }

    @GetMapping(ROUTE_MYPAGE + ROUTE_EDIT)
    public String mypageEditor(final Model model, final HttpSession session) {
        final User user = (User) session.getAttribute(USER);
        model.addAttribute("user", user);
        return loginFirstOr(PAGE_MYPAGE_EDIT, session);
    }

    @PutMapping(ROUTE_MYPAGE + ROUTE_EDIT)
    public String mypageEdit(final Model model, final HttpSession session, final MyPageEditDto dto) {
        try {
            final User loginUser = (User) session.getAttribute(USER);
            final User user = userRepository.findById(loginUser.getId()).get();
            LOG.debug("user: {}", user);
            LOG.debug("DTO: {}", dto);
            user.setUsername(dto.getUsername());
            userRepository.save(user);
            session.setAttribute(USER, user);
            return REDIRECT + ROUTE_MYPAGE;
        } catch (Exception e) {
            return REDIRECT + ROUTE_LOGIN;
        }
    }
}
