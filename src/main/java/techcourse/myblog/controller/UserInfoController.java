package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.support.validation.UserGroups;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mypage")
public class UserInfoController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    public UserInfoController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String myPageForm() {
        return "mypage";
    }

    @GetMapping("/edit")
    public String myPageEditForm(HttpServletRequest request, Model model, UserDto userDto) {
        model.addAttribute("user", request.getSession().getAttribute("user"));
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public String myPageEdit(HttpServletRequest request, @Validated(UserGroups.Edit.class) UserDto userDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", request.getSession().getAttribute("user"));
            return "mypage-edit";
        }
        User user = (User) request.getSession().getAttribute("user");
        userService.update(user, userDto);

        return "redirect:/mypage";
    }

    @DeleteMapping("/withdraw")
    public String myPageDelete(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        userService.deleteById(user.getId());
        return "redirect:/users/logout";
    }
}
