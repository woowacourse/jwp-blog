package techcourse.myblog.support.validator;

import techcourse.myblog.application.ArticleService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleConstraintValidator implements ConstraintValidator<TitleConstraint, String> {
    private final ArticleService articleService;

    public TitleConstraintValidator(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean isTitleExist = articleService.isTitleExist(value);
        if (isTitleExist) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("게시글 제목은 중복될 수 없습니다")
                .addConstraintViolation();
            return false;
        }
        
        return true;
    }
}
