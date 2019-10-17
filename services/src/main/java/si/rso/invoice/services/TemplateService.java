package si.rso.invoice.services;

import java.util.Map;

public interface TemplateService {
    
    String renderTemplate(String templateName, Map<String, Object> params);
    
}
