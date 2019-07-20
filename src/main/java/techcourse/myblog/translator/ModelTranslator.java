package techcourse.myblog.translator;

public interface ModelTranslator<T, Dto> {
    T toEntity(T t, Dto dto);
}
