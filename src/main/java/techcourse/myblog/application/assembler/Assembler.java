package techcourse.myblog.application.assembler;

public interface Assembler<E, D> {
    D convertEntityToDto(E entity);
}