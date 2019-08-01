package techcourse.myblog.translator;

public interface ModelTranslator<T, Dto> {
    T toEntity(T t, Dto dto);

    Dto toDto(T t, Dto dto);
}
