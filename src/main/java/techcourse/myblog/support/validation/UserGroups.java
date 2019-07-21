package techcourse.myblog.support.validation;

import javax.validation.groups.Default;

public interface UserGroups {
    interface Edit{}
    interface All extends Default {}
}
