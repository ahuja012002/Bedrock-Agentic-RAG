package com.manufacturing.chatbot.manufacturing_chatbot;

import java.util.HashMap;
import java.util.Map;

public class ActionResponse {
    private boolean success;
    private String message;
    private Object data;
    private Map<String, Object> metadata;

    public ActionResponse() {
        this.metadata = new HashMap<>();
    }

    public static ActionResponse success(Object data, String message) {
        ActionResponse response = new ActionResponse();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static ActionResponse error(String message) {
        ActionResponse response = new ActionResponse();
        response.success = false;
        response.message = message;
        return response;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}