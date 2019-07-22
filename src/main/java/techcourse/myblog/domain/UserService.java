package techcourse.myblog.domain;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        iterable.forEach(allUsers::add);
        return allUsers;
    }

    public RedirectView create(UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userDto", userDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return new RedirectView("/signup");
        }

        try {
            userRepository.save(userDto.toUser());
        } catch (DataIntegrityViolationException e) {
            bindingResult.addError(new FieldError("userDto", "email", "이미 존재하는 email입니다."));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return new RedirectView("/signup");
        }

        return new RedirectView("/login");
    }

    public RedirectView update(UserDto userDto, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return new RedirectView("/mypage/edit");
        }

        user.modifyName(userDto.getName());
        userRepository.save(user);
        return new RedirectView("/mypage");
    }

    public RedirectView login(UserDto userDto, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<User> loginUser = userRepository.findByEmail(userDto.getEmail());

        if (!loginUser.isPresent()) {
            bindingResult.addError(new FieldError("userDto", "email", "이메일을 확인해주세요."));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return new RedirectView("/login");
        }

        if (!loginUser.get().matchPassword(userDto.toUser())) {
            bindingResult.addError(new FieldError("userDto", "password", "비밀번호를 확인해주세요."));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return new RedirectView("/login");
        }

        session.setAttribute("user", loginUser.get());
        return new RedirectView("/");
    }

    public RedirectView delete(User user) {
        if (user != null && user.matchEmail(user)) {
            userRepository.delete(user);
        }
        return new RedirectView("/");
    }
}
