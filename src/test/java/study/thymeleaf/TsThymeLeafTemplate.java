package study.thymeleaf;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.HashMap;
import java.util.Map;

public class TsThymeLeafTemplate {
    private TemplateEngine templateEngine;
    private Map<String, Object> variables;

    public TsThymeLeafTemplate() {
        templateEngine = createTemplateEngine();
        variables = new HashMap<>();
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public String process(String template) {
        Context ctx = new Context();
        ctx.setVariables(variables);
        return templateEngine.process(template, ctx);
    }

    private static TemplateEngine createTemplateEngine() {
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
