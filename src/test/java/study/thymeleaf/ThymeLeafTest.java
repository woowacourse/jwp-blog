package study.thymeleaf;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class ThymeLeafTest {
    private static final Logger log = LoggerFactory.getLogger( ThymeLeafTest.class );

    @Test
    void template_welcome() {
        TemplateEngine templateEngine = createTemplateEngine();

        Context ctx = new Context();
        String welcomeMessage = "Hi World";
        ctx.setVariable("welcome", welcomeMessage);
        String result = templateEngine.process("welcome", ctx);
        log.debug("{}", result);
        assertThat(result).contains(welcomeMessage);
    }

    @Test
    void template_no_parameter() {
        TemplateEngine templateEngine = createTemplateEngine();

        Context ctx = new Context();
        String result = templateEngine.process("welcome", ctx);
        log.debug("{}", result);
    }

    @Test
    void javabean() {
        TemplateEngine templateEngine = createTemplateEngine();

        Context ctx = new Context();
        MyBean myBean = new MyBean("javajigi", "javajigi@slipp.net");
        ctx.setVariable("myBean", myBean);
        String result = templateEngine.process("mybean", ctx);
        log.debug("{}", result);
        assertThat(result).contains(myBean.getName());
        assertThat(result).contains(myBean.getEmail());
    }

    @Test
    void layout() {
        TemplateEngine templateEngine = createTemplateEngine();

        Context ctx = new Context();
        String result = templateEngine.process("index", ctx);
        log.debug("{}", result);
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
