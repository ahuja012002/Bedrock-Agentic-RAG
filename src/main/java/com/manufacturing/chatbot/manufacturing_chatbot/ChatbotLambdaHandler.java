package com.manufacturing.chatbot.manufacturing_chatbot;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class ChatbotLambdaHandler implements RequestHandler<Map<String, Object>, Object> {

    private static ConfigurableApplicationContext applicationContext;
    private ObjectMapper objectMapper;
    
    public ChatbotLambdaHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {
        if (applicationContext == null) {
            applicationContext = SpringApplication.run(com.manufacturing.chatbot.manufacturing_chatbot.ManufacturingChatbotApplication.class);
        }

        try {
            context.getLogger().log("Received input: " + objectMapper.writeValueAsString(input));
            
            ActionController actionController = applicationContext.getBean(ActionController.class);
            
            // Parse the input to determine the action
            String actionGroup = (String) input.get("actionGroup");
            String apiPath = (String) input.get("apiPath");
            String httpMethod = (String) input.get("httpMethod");
            
            // Extract parameters from the input - FIXED THIS METHOD
            Map<String, Object> parameters = extractParameters(input, context);
            
            // Create ActionRequest
            ActionRequest actionRequest = new ActionRequest();
            actionRequest.setActionGroup(actionGroup);
            actionRequest.setApiPath(apiPath);
            actionRequest.setHttpMethod(httpMethod);
            actionRequest.setParameters(parameters);

            ActionResponse response = actionController.handleAction(actionRequest);
            
            return Map.of(
                "messageVersion", "1.0",
                "response", Map.of(
                    "actionGroup", actionGroup,
                    "apiPath", apiPath,
                    "httpMethod", httpMethod,
                    "httpStatusCode", 200,
                    "responseBody", Map.of(
                        "application/json", Map.of(
                            "body", objectMapper.writeValueAsString(response)
                        )
                    )
                )
            );
            
        } catch (Exception e) {
            context.getLogger().log("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return Map.of(
                "messageVersion", "1.0",
                "response", Map.of(
                    "actionGroup", input.get("actionGroup"),
                    "apiPath", input.get("apiPath"), 
                    "httpMethod", input.get("httpMethod"),
                    "httpStatusCode", 500,
                    "responseBody", Map.of(
                        "application/json", Map.of(
                            "body", "{\"error\": \"" + e.getMessage() + "\"}"
                        )
                    )
                )
            );
        }
    }
    
    private Map<String, Object> extractParameters(Map<String, Object> input, Context context) {
        Map<String, Object> parameters = new HashMap<>();
        
        try {
            context.getLogger().log("Starting parameter extraction...");
            
            // METHOD 1: Extract from the nested structure (requestBody -> content -> application/json -> properties)
            Object requestBodyObj = input.get("requestBody");
            if (requestBodyObj instanceof Map) {
                Map<String, Object> requestBody = (Map<String, Object>) requestBodyObj;
                Object contentObj = requestBody.get("content");
                
                if (contentObj instanceof Map) {
                    Map<String, Object> content = (Map<String, Object>) contentObj;
                    Object applicationJsonObj = content.get("application/json");
                    
                    if (applicationJsonObj instanceof Map) {
                        Map<String, Object> applicationJson = (Map<String, Object>) applicationJsonObj;
                        Object propertiesObj = applicationJson.get("properties");
                        
                        if (propertiesObj instanceof List) {
                            List<Map<String, Object>> properties = (List<Map<String, Object>>) propertiesObj;
                            context.getLogger().log("Found " + properties.size() + " properties in application/json");
                            
                            for (Map<String, Object> property : properties) {
                                String name = (String) property.get("name");
                                Object value = property.get("value");
                                String type = (String) property.get("type");
                                
                                if (name != null && value != null) {
                                    // Convert value based on type if needed
                                    Object convertedValue = convertValueBasedOnType(value, type);
                                    parameters.put(name, convertedValue);
                                    context.getLogger().log("Extracted parameter: " + name + " = " + convertedValue + " (type: " + type + ")");
                                }
                            }
                        }
                    }
                }
            }
            
            // METHOD 2: Check if parameters array exists at root level (alternative format)
            if (parameters.isEmpty()) {
                Object parametersObj = input.get("parameters");
                if (parametersObj instanceof List) {
                    List<?> paramsList = (List<?>) parametersObj;
                    context.getLogger().log("Found parameters as list with " + paramsList.size() + " items");
                    
                    for (Object paramObj : paramsList) {
                        if (paramObj instanceof Map) {
                            Map<String, Object> param = (Map<String, Object>) paramObj;
                            String name = (String) param.get("name");
                            Object value = param.get("value");
                            if (name != null && value != null) {
                                parameters.put(name, value);
                                context.getLogger().log("Parameter from array: " + name + " = " + value);
                            }
                        }
                    }
                }
            }
            
            // METHOD 3: Extract session attributes if available
            Object sessionAttributesObj = input.get("sessionAttributes");
            if (sessionAttributesObj instanceof Map) {
                Map<String, Object> sessionAttributes = (Map<String, Object>) sessionAttributesObj;
                parameters.putAll(sessionAttributes);
                context.getLogger().log("Added session attributes: " + sessionAttributes);
            }
            
            // METHOD 4: Add other useful metadata
            addMetadataParameters(input, parameters);
            
            context.getLogger().log("Final extracted parameters: " + objectMapper.writeValueAsString(parameters));
            
        } catch (Exception e) {
            context.getLogger().log("Error extracting parameters: " + e.getMessage());
            e.printStackTrace();
        }
        
        return parameters;
    }
    
    private Object convertValueBasedOnType(Object value, String type) {
        if (type == null || value == null) {
            return value;
        }
        
        try {
            switch (type.toLowerCase()) {
                case "integer":
                case "int":
                    if (value instanceof String) {
                        return Integer.parseInt((String) value);
                    }
                    return value;
                    
                case "boolean":
                case "bool":
                    if (value instanceof String) {
                        return Boolean.parseBoolean((String) value);
                    }
                    return value;
                    
                case "double":
                case "float":
                    if (value instanceof String) {
                        return Double.parseDouble((String) value);
                    }
                    return value;
                    
                case "array":
                case "list":
                    if (value instanceof String) {
                        return objectMapper.readValue((String) value, List.class);
                    }
                    return value;
                    
                case "object":
                case "map":
                    if (value instanceof String) {
                        return objectMapper.readValue((String) value, Map.class);
                    }
                    return value;
                    
                default:
                    return value;
            }
        } catch (Exception e) {
            return value; // Return original value if conversion fails
        }
    }
    
    private void addMetadataParameters(Map<String, Object> input, Map<String, Object> parameters) {
        // Add useful metadata that might be needed by actions
        if (input.containsKey("sessionId")) {
            parameters.put("sessionId", input.get("sessionId"));
        }
        if (input.containsKey("inputText")) {
            parameters.put("inputText", input.get("inputText"));
        }
        if (input.containsKey("agent")) {
            parameters.put("agent", input.get("agent"));
        }
        if (input.containsKey("actionGroup")) {
            parameters.put("actionGroup", input.get("actionGroup"));
        }
    }
}