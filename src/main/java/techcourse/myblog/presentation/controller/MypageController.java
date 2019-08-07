package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.UserWriteService;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.user.validation.UserInfo;
import techcourse.myblog.presentation.support.LoginUser;

@Controller
@RequestMapping("/mypage")
public class MypageController {
    private final UserWriteService userWriteService;
    
    public MypageController(UserWriteService userWriteService) {
        this.userWriteService = userWriteService;
    }
    
    @GetMapping
    public String myPage() {
        return "mypage";
    }
    
    @GetMapping("/edit")
    public String createMyPageForm(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }
        return "mypage-edit";
    }
    
    @PutMapping
    public RedirectView editUser(LoginUser loginUser,
                                 @ModelAttribute("/mypage/edit") @Validated(UserInfo.class) UserDto userDto) {
        userWriteService.update(loginUser.getUser(), userDto.toUser());
        
        return new RedirectView("/mypage");
    }
    
    @DeleteMapping
    public RedirectView removeUser(LoginUser loginUser) {
        userWriteService.remove(loginUser.getUser());
        
        return new RedirectView("/logout");
    }
}
