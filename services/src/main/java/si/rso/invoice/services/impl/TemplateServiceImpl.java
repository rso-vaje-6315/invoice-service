package si.rso.invoice.services.impl;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import si.rso.invoice.services.TemplateService;
import si.rso.rest.exceptions.RestException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class TemplateServiceImpl implements TemplateService {
    
    private TemplateEngine templateEngine;
    
    @PostConstruct
    private void init() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
    
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        
        this.templateEngine = engine;
    }
    
    @Override
    public String renderTemplate(String templateName, Map<String, Object> params) {
    
        Context context = new Context();
        params.keySet().forEach(key -> context.setVariable(key, params.get(key)));
        
        try {
            return this.templateEngine.process(templateName, context);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestException("Error generating template!");
        }
    }
}
