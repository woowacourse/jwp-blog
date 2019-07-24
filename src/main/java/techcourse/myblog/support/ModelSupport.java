package techcourse.myblog.support;

import org.springframework.ui.Model;

public class ModelSupport {
    public static void addObjectDoesNotContain(Model model, String name, Object object) {
        if (!model.containsAttribute(name)) {
            model.addAttribute(name, object);
        }
    }
}
