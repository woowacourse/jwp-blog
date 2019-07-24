package techcourse.myblog.utils.page;

import org.springframework.data.domain.Sort;

public class PageRequest {
    private static final int MIN_PAGE = 0;
    private static final int DEFAULT_PAGE = 1;
    private static final int MAX_SIZE = 10;

    private int page;
    private int size;
    private Sort.Direction direction;
    private String orderby;

    public PageRequest(int page, int size, Sort.Direction direction, String orderby) {
        this.page = page < MIN_PAGE ? DEFAULT_PAGE : page;
        this.size = size > MAX_SIZE ? MAX_SIZE : size;
        this.direction = direction;
        this.orderby = orderby;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, orderby);
    }
}
