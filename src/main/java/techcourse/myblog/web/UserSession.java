package techcourse.myblog.web;

import techcourse.myblog.domain.User;

//TODO 불변객체로 만들었는데 과연 괜찮은 판단일까?
//생성자의 매개변수로 User를 받는 것은 괜찮을까 ?
//아니면 DTO와 Entity의 변환처럼 메서드를 빼는게 나을까?
public class UserSession {
    private final String name;
    private final String email;

    public UserSession(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserSession(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public UserSession updateName(String name) {
        return new UserSession(name, this.email);
    }

    public UserSession updateEmail(String email) {
        return new UserSession(this.name, email);
    }
}
