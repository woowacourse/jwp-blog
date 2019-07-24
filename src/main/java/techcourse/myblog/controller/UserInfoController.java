package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserWriteService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.ModelSupport;
import techcourse.myblog.support.RedirectAttributeSupport;
import techcourse.myblog.support.argument.resolver.SessionInfo;
import techcourse.myblog.support.validation.UserGroups.Edit;

@Controller
@RequestMapping("/mypage")
public class UserInfoController {
    private final UserWriteService userWriteService;

    public UserInfoController(final UserWriteService userWriteService) {
        this.userWriteService = userWriteService;
    }

    @GetMapping
    public String myPageForm() {
        return "mypage";
    }

    @GetMapping("/edit")
    public String myPageEditForm(Model model, SessionInfo sessionInfo) {
        ModelSupport.addObjectDoesNotContain(model, "userDto", new UserDto("", "", ""));
        model.addAttribute("user", sessionInfo.getUser());
        return "mypage-edit";
    }

    @PutMapping("/edit")
    public RedirectView myPageEdit(@ModelAttribute("/mypage/edit") @Validated(Edit.class) UserDto userDto,
                                   BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                                   SessionInfo sessionInfo) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", sessionInfo.getUser());
            RedirectAttributeSupport.addBindingResult(redirectAttributes, bindingResult, "userDto", userDto);
            return new RedirectView("/edit");
        }

        userWriteService.update(sessionInfo.getUser(), userDto);

        return new RedirectView("/mypage");
    }

    @DeleteMapping("/withdraw")
    public RedirectView myPageDelete(SessionInfo sessionInfo) {
        User user = sessionInfo.getUser();
        userWriteService.deleteById(user.getId());
        return new RedirectView("/users/logout");
    }
}
