package techcourse.myblog.support;

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalConsumer<T> {
    private Optional<T> optional;

    private OptionalConsumer(Optional<T> optional) {
        this.optional = optional;
    }

    public static <T> OptionalConsumer<T> of(Optional<T> optional) {
        return new OptionalConsumer<>(optional);
    }

    public void ifPresent(Consumer<? super T> consumer) {
        optional.ifPresent(consumer);
    }

    public void ifNotPresent(Runnable emptyAction) {
        if (isNotPresent()) {
            emptyAction.run();
        }
    }

    private boolean isNotPresent() {
        return !optional.isPresent();
    }
}
