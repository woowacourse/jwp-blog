package techcourse.myblog.controller.session;

interface SessionManager {
    void set(String sessionKey, Object sessionValue);

    Object get(String sessionKey);

    void remove(String sessionKey);
}
