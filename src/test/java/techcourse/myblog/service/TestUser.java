package techcourse.myblog.service;

import techcourse.myblog.domain.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TestUser {
    private static final Long EMPTY_ID = -1l;

    public static final String VALID_NAME = "valid";
    public static final String VALID_EMAIL = "valid@test.com";
    public static final String VALID_PASSWORD = "123123123";

    public static User createValid() {
        return User.builder()
                .id(EMPTY_ID)
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .build();
    }

    private static User createAs(Long id) {
        return User.builder()
                .id(id)
                .name(VALID_NAME + "_" + id)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .build();
    }

    public static List<User> createValidUsers(int numUser) {
        return LongStream.range(0, numUser)
                .mapToObj(TestUser::createAs)
                .collect(Collectors.toList());
    }

}
