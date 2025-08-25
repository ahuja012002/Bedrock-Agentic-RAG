package com.manufacturing.chatbot.manufacturing_chatbot;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WarrantyInfo {
    private String serialNumber;
    private String productModel;
    private String customerId;
    private String purchaseDate;  // String for JSON serialization
    private String warrantyExpiry;  // String for JSON serialization
    private String warrantyType;
    private List<String> coveredComponents;
    private boolean isActive;
    private String status;
    private String message;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Getters and setters
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    public String getProductModel() { return productModel; }
    public void setProductModel(String productModel) { this.productModel = productModel; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }
    
    // Helper method to set LocalDate and convert to String
    public void setPurchaseDate(LocalDate purchaseDate) { 
        this.purchaseDate = purchaseDate != null ? purchaseDate.format(DATE_FORMATTER) : null; 
    }
    
    // Helper method to get LocalDate from String - excluded from JSON serialization
    @JsonIgnore
    public LocalDate getPurchaseDateAsLocalDate() {
        return purchaseDate != null ? LocalDate.parse(purchaseDate, DATE_FORMATTER) : null;
    }
    
    public String getWarrantyExpiry() { return warrantyExpiry; }
    public void setWarrantyExpiry(String warrantyExpiry) { this.warrantyExpiry = warrantyExpiry; }
    
    // Helper method to set LocalDate and convert to String
    public void setWarrantyExpiry(LocalDate warrantyExpiry) { 
        this.warrantyExpiry = warrantyExpiry != null ? warrantyExpiry.format(DATE_FORMATTER) : null; 
    }
    
    // Helper method to get LocalDate from String - excluded from JSON serialization
    @JsonIgnore
    public LocalDate getWarrantyExpiryAsLocalDate() {
        return warrantyExpiry != null ? LocalDate.parse(warrantyExpiry, DATE_FORMATTER) : null;
    }
    
    public String getWarrantyType() { return warrantyType; }
    public void setWarrantyType(String warrantyType) { this.warrantyType = warrantyType; }
    
    public List<String> getCoveredComponents() { return coveredComponents; }
    public void setCoveredComponents(List<String> coveredComponents) { this.coveredComponents = coveredComponents; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}