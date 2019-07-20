package study.thymeleaf;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThymeLeafSampleTest {
    private TsThymeLeafTemplate tsThymeLeafTemplate = new TsThymeLeafTemplate();

    @Test
    void with_variable() {
        tsThymeLeafTemplate.setVariable("welcome", "javajigi");
        String actual = tsThymeLeafTemplate.process("samples/test1");
        assertThat(actual).isEqualTo(format("javajigi"));
    }

    @Test
    void no_variable() {
        String actual = tsThymeLeafTemplate.process("samples/test1");
        assertThat(actual).isEqualTo(format(""));
    }

    @Test
    void with_messages() {
        String actual = tsThymeLeafTemplate.process("samples/test2");
        assertThat(actual).contains(format("Hello World!"));
    }

    @Test
    void with_messages_unescape() {
        String actual = tsThymeLeafTemplate.process("samples/test2");
        assertThat(actual).contains(format("Hello World <b>javajigi</b>!"));
    }

    @Test
    void with_messages_params() {
        tsThymeLeafTemplate.setVariable("nickname", "javajigi");
        String actual = tsThymeLeafTemplate.process("samples/test2");
        assertThat(actual).contains(format("Hello World javajigi!"));
    }

    @Test
    void javabean_variable() {
        TsUser tsUser = new TsUser("javajigi", "javajigi@slipp.net", 35);
        tsThymeLeafTemplate.setVariable("tsUser", tsUser);
        String actual = tsThymeLeafTemplate.process("samples/test3");
        assertThat(actual).contains(format(tsUser.getName()));
        assertThat(actual).contains(format(tsUser.getEmail()));
        assertThat(actual).contains(format(tsUser.getAge()));
    }

    @Test
    void javabean_selection_variable() {
        TsUser tsUser = new TsUser("javajigi", "javajigi@slipp.net", 35);
        tsThymeLeafTemplate.setVariable("tsUser", tsUser);
        String actual = tsThymeLeafTemplate.process("samples/test4");
        assertThat(actual).contains(format(tsUser.getName()));
        assertThat(actual).contains(format(tsUser.getEmail()));
        assertThat(actual).contains(format(tsUser.getAge()));
    }

    @Test
    void literal() {
        tsThymeLeafTemplate.setVariable("nickname", "javajigi");
        String actual = tsThymeLeafTemplate.process("samples/test5");
        assertThat(actual).contains(format("Hello World javajigi!"));
    }

    private String format(Object arg) {
        return String.format("<p>%s</p>", arg);
    }
}
