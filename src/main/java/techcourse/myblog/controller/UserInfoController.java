package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.ModelSupport;
import techcourse.myblog.support.RedirectAttributeSupport;
import techcourse.myblog.support.validation.UserGroups.Edit;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mypage")
public class UserInfoController {
    private final UserService userService;

    public UserInfoController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String myPageForm() {
        return "mypage";
    }

    @GetMapping("/edit")
    public String myPageEditForm(HttpServletRequest request, Model model) {
        ModelSupport.addObjectDoesNotContain(model, "userDto", new UserDto("", "", ""));
        model.addAttribute("user", request.getSession().getAttribute("user"));
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView myPageEdit(HttpServletRequest request, @Validated(Edit.class) UserDto userDto,
                                   BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", request.getSession().getAttribute("user"));
            RedirectAttributeSupport.addBindingResult(redirectAttributes, bindingResult, "userDto", userDto);
            return new RedirectView("/edit");
        }

        User user = (User) request.getSession().getAttribute("user");
        userService.update(user.getId(), userDto).ifPresent(updateUser -> {
            request.getSession().setAttribute("user", updateUser);
        });

        return new RedirectView("/mypage");
    }

    @DeleteMapping("/withdraw")
    public RedirectView myPageDelete(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        userService.deleteById(user.getId());
        return new RedirectView("/users/logout");
    }
}
