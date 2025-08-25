package com.manufacturing.chatbot.manufacturing_chatbot;

import com.manufacturing.chatbot.manufacturing_chatbot.ProductSpecification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    // Simulated product database
    private final Map<String, ProductSpecification> productDatabase = new HashMap<>();

    public ProductService() {
        // Initialize with sample data
        initializeProductData();
    }

    public ProductSpecification getProductSpecifications(String productModel, String specificationType) {
        ProductSpecification product = productDatabase.get(productModel.toUpperCase());
        
        if (product == null) {
            // Create a generic "not found" response
            ProductSpecification notFound = new ProductSpecification();
            notFound.setProductModel(productModel);
            notFound.setCategory("Unknown");
            notFound.setDescription("Product not found in database. Please verify the model number or contact support.");
            return notFound;
        }
        
        return product;
    }

    private void initializeProductData() {
        // Industrial Pump XM-2000
        ProductSpecification pump = new ProductSpecification();
        pump.setProductModel("XM-2000");
        pump.setCategory("Industrial Pumps");
        pump.setDescription("High-performance centrifugal pump for industrial applications");
        
        Map<String, String> pumpSpecs = new HashMap<>();
        pumpSpecs.put("Flow Rate", "500-2000 GPM");
        pumpSpecs.put("Head", "50-200 feet");
        pumpSpecs.put("Power", "25-100 HP");
        pumpSpecs.put("Inlet Size", "6-12 inches");
        pumpSpecs.put("Outlet Size", "4-10 inches");
        pumpSpecs.put("Temperature Range", "-20°F to 400°F");
        pumpSpecs.put("Pressure Rating", "150-600 PSI");
        pump.setTechnicalSpecs(pumpSpecs);
        
        pump.setFeatures(List.of(
            "Corrosion-resistant coating",
            "Self-priming design", 
            "Low maintenance requirements",
            "Energy efficient motor",
            "Digital monitoring system"
        ));
        
        pump.setInstallationGuide("Refer to Installation Manual IM-XM2000-v3.2");
        pump.setMaintenanceSchedule("Quarterly inspection, Annual overhaul");
        
        productDatabase.put("XM-2000", pump);

        // Conveyor Belt CB-500
        ProductSpecification conveyor = new ProductSpecification();
        conveyor.setProductModel("CB-500");
        conveyor.setCategory("Material Handling");
        conveyor.setDescription("Heavy-duty conveyor belt system for material transport");
        
        Map<String, String> conveyorSpecs = new HashMap<>();
        conveyorSpecs.put("Belt Width", "18-48 inches");
        conveyorSpecs.put("Length", "10-100 feet");
        conveyorSpecs.put("Load Capacity", "1000-5000 lbs");
        conveyorSpecs.put("Speed", "10-200 ft/min");
        conveyorSpecs.put("Motor Power", "5-50 HP");
        conveyorSpecs.put("Belt Material", "Rubber, PVC, or Steel mesh");
        conveyor.setTechnicalSpecs(conveyorSpecs);
        
        conveyor.setFeatures(List.of(
            "Variable speed control",
            "Emergency stop systems",
            "Modular design",
            "Low noise operation",
            "Easy maintenance access"
        ));
        
        conveyor.setInstallationGuide("Refer to Installation Manual IM-CB500-v2.1");
        conveyor.setMaintenanceSchedule("Weekly belt inspection, Monthly lubrication");
        
        productDatabase.put("CB-500", conveyor);

        // Hydraulic Press HP-1000
        ProductSpecification press = new ProductSpecification();
        press.setProductModel("HP-1000");
        press.setCategory("Hydraulic Equipment");
        press.setDescription("High-pressure hydraulic press for forming and stamping operations");
        
        Map<String, String> pressSpecs = new HashMap<>();
        pressSpecs.put("Maximum Force", "1000 tons");
        pressSpecs.put("Bed Size", "48\" x 36\"");
        pressSpecs.put("Stroke Length", "24 inches");
        pressSpecs.put("Hydraulic Pressure", "3000 PSI");
        pressSpecs.put("Power Requirements", "460V, 3-phase, 100A");
        pressSpecs.put("Control System", "PLC with HMI touchscreen");
        press.setTechnicalSpecs(pressSpecs);
        
        press.setFeatures(List.of(
            "Programmable stroke control",
            "Safety light curtains",
            "Precision pressure control",
            "Auto-cycling capability",
            "Remote monitoring ready"
        ));
        
        press.setInstallationGuide("Refer to Installation Manual IM-HP1000-v4.0");
        press.setMaintenanceSchedule("Daily pressure checks, Monthly filter changes");
        
        productDatabase.put("HP-1000", press);
    }
}