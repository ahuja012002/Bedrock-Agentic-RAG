package com.manufacturing.chatbot.manufacturing_chatbot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionRequest {
    private String actionGroup;
    private String apiPath;
    private String httpMethod;
    private Map<String, Object> parameters;

    public ActionRequest() {
        this.parameters = new HashMap<>();
    }

    // Getters and setters
    public String getActionGroup() { return actionGroup; }
    public void setActionGroup(String actionGroup) { this.actionGroup = actionGroup; }
    
    public String getApiPath() { return apiPath; }
    public void setApiPath(String apiPath) { this.apiPath = apiPath; }
    
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    
    public Map<String, Object> getParameters() { 
        return parameters != null ? parameters : new HashMap<>(); 
    }
    public void setParameters(Map<String, Object> parameters) { 
        this.parameters = parameters != null ? parameters : new HashMap<>(); 
    }
    
    public String getParameter(String key) {
        if (parameters == null) return null;
        Object value = parameters.get(key);
        return value != null ? value.toString() : null;
    }
    
    public void addParameter(String key, Object value) {
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(key, value);
    }
}