package techcourse.myblog.controller.session;

interface SessionManagerInterface {
    void set(String sessionKey, Object sessionValue);
    Object get(String sessionKey);
    void remove(String sessionKey);
}
