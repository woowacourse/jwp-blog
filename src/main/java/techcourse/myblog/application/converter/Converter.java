package techcourse.myblog.application.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Converter<T, S, U> {
    U convertFromDto(final T dto);

    S convertFromEntity(final U entity);

    default List<U> createFromDtos(final Collection<T> dtos) {
        return dtos.stream().map(this::convertFromDto).collect(Collectors.toList());
    }

    default List<S> createFromEntities(final Collection<U> entities) {
        return entities.stream().map(this::convertFromEntity).collect(Collectors.toList());
    }
}