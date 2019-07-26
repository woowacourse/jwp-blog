package techcourse.myblog.service.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Converter<T, U> {
    private final Function<T, U> toEntity;
    private final Function<U, T> toDto;

    public Converter(final Function<T, U> toEntity, final Function<U, T> toDto){
        this.toEntity = toEntity;
        this.toDto = toDto;
    }

    public final U convertToEntity(final T dto){
        return toEntity.apply(dto);
    }

    public final T convertToDto(final U entity){
        return toDto.apply(entity);
    }

    public final List<T> convertToEntities(final Collection<U> entities){
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList())
                ;
    }
}
