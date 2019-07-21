package techcourse.myblog.support;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class RedirectAttibuteSupport {
    public static void addFlashAttribute(String name, Object object, RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + name, bindingResult);
        redirectAttributes.addFlashAttribute(name, object);
    }
}
