package techcourse.myblog.service;

import techcourse.myblog.domain.Domain;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UnauthorizedException;

abstract class DomainAbstractService {

    protected abstract Domain getAuthorizedDomain(Long id, User user);

    void checkAuthorizedUser(User user, Domain domain) {
        if (user == null || !domain.isAuthorized(user)) {
            throw new UnauthorizedException();
        }
    }
}
