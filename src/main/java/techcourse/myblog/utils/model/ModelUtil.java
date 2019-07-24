package techcourse.myblog.utils.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class ModelUtil {
    private static final Logger log = LoggerFactory.getLogger(ModelUtil.class);
    private static final String BEFORE_MODEL = "Before Model : {} ";
    private static final String AFTER_MODEL = "After Model : {} ";

    public static void addAttribute(Model model, String attribute, Object object) {
        log.info(BEFORE_MODEL, model);
        model.addAttribute(attribute, object);
        log.info(AFTER_MODEL, model);
    }
}
