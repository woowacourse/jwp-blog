package techcourse.myblog.presentation.support;

public enum RedirectUrl {
    LOGIN("/login"),
    SIGN_UP("/signup"),
    MY_PAGE_EDIT("/mypage/edit");

    private final String url;

    RedirectUrl(String url) {
        this.url = url;
    }

    public static RedirectUrl convert(String target) {
        if (target.equals("loginUserDto")) {
            return LOGIN;
        }

        if (target.equals("signUpUserDto")) {
            return SIGN_UP;
        }

        if (target.equals("editUserDto")) {
            return MY_PAGE_EDIT;
        }

        throw new IllegalArgumentException("존재하지 않는 타겟입니다.");
    }

    public String getUrl() {
        return url;
    }
}
