package techcourse.myblog.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO:
    // 1. Exception 에서 중요한 정보 얻어내기 (ex. stackTrace 내부에서 필요할 것 같은 정보 추출)
    // 2. 각 Exception 종류에 대한 handler 구현하기 (참고: https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f)
    @ExceptionHandler
    public final String handleException(Exception ex, WebRequest request, Model model) {
        log.info("+++++++++++++++++++++++++ handleException called..!!");

        log.info("ex: " + ex.toString());

        model.addAttribute("error", new ApiError(ex.toString()));

        return "error";
    }
}
