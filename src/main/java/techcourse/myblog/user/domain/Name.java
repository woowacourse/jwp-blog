package techcourse.myblog.user.domain;

public class Name {
    private static final String NAME_REGEX = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}";

    private String name;

    private Name(final String name) {
        this.name = validateName(name);
    }

    public static Name of(final String name) {
        return new Name(name);
    }

    private String validateName(final String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("이름은 2자 이상 10자 이하입니다.");
        }
        return name;
    }

    public void updateName(String name) {
        this.name = validateName(name);
    }

    public String getName() {
        return name;
    }
}