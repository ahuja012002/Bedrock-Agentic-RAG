package com.manufacturing.chatbot.manufacturing_chatbot;

import java.util.List;
import java.util.Map;

public class ProductSpecification {
    private String productModel;
    private String category;
    private String description;
    private Map<String, String> technicalSpecs;
    private List<String> features;
    private String installationGuide;
    private String maintenanceSchedule;

    // Getters and setters
    public String getProductModel() { return productModel; }
    public void setProductModel(String productModel) { this.productModel = productModel; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Map<String, String> getTechnicalSpecs() { return technicalSpecs; }
    public void setTechnicalSpecs(Map<String, String> technicalSpecs) { this.technicalSpecs = technicalSpecs; }
    
    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    
    public String getInstallationGuide() { return installationGuide; }
    public void setInstallationGuide(String installationGuide) { this.installationGuide = installationGuide; }
    
    public String getMaintenanceSchedule() { return maintenanceSchedule; }
    public void setMaintenanceSchedule(String maintenanceSchedule) { this.maintenanceSchedule = maintenanceSchedule; }
}