package techcourse.myblog.support;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class RedirectAttributeSupport {
    public static void addBindingResult(RedirectAttributes redirectAttributes, BindingResult bindingResult, String name, Object object) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + name, bindingResult);
        redirectAttributes.addFlashAttribute(name, object);
    }
}
