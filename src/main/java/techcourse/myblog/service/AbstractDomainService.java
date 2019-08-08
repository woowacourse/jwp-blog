package techcourse.myblog.service;

import techcourse.myblog.domain.AbstractDomain;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UnauthorizedException;

abstract class AbstractDomainService<T> {

    public abstract T save(T domain);

    public abstract void delete(Long id, User user);

    protected abstract T getAuthorizedDomain(Long id, User user);

    void checkAuthorizedUser(User user, AbstractDomain domain) {
        if (user == null || !domain.isAuthorized(user)) {
            throw new UnauthorizedException();
        }
    }
}
