package com.manufacturing.chatbot.manufacturing_chatbot;

import com.manufacturing.chatbot.manufacturing_chatbot.WarrantyInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WarrantyService {

    private final Map<String, WarrantyInfo> warrantyDatabase = new HashMap<>();

    public WarrantyService() {
        initializeWarrantyData();
    }

    public WarrantyInfo checkWarrantyStatus(String serialNumber, String customerId) {
        WarrantyInfo warranty = warrantyDatabase.get(serialNumber);
        
        if (warranty == null) {
            // Create a "not found" response
            WarrantyInfo notFound = new WarrantyInfo();
            notFound.setSerialNumber(serialNumber);
            notFound.setStatus("NOT_FOUND");
            notFound.setMessage("Warranty information not found for serial number: " + serialNumber);
            return notFound;
        }
        
        // Update warranty status based on current date
        updateWarrantyStatus(warranty);
        return warranty;
    }

    private void updateWarrantyStatus(WarrantyInfo warranty) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = warranty.getWarrantyExpiryAsLocalDate();
        
        if (expiryDate != null && (currentDate.isBefore(expiryDate) || currentDate.isEqual(expiryDate))) {
            warranty.setActive(true);
            warranty.setStatus("ACTIVE");
        } else {
            warranty.setActive(false);
            warranty.setStatus("EXPIRED");
        }
        
        if (expiryDate != null) {
            long daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(currentDate, expiryDate);
            if (daysRemaining < 0) {
                warranty.setMessage("Warranty expired " + Math.abs(daysRemaining) + " days ago");
            } else if (daysRemaining <= 30) {
                warranty.setMessage("Warranty expires in " + daysRemaining + " days");
            } else {
                warranty.setMessage("Warranty is active with " + daysRemaining + " days remaining");
            }
        }
    }

    private void initializeWarrantyData() {
        // Sample warranty data
        WarrantyInfo warranty1 = new WarrantyInfo();
        warranty1.setSerialNumber("XM2000-2024-001");
        warranty1.setProductModel("XM-2000");
        warranty1.setCustomerId("CUST-12345");
        warranty1.setPurchaseDate(LocalDate.of(2024, 6, 15));
        warranty1.setWarrantyExpiry(LocalDate.of(2026, 6, 15));
        warranty1.setWarrantyType("Standard 2-Year");
        warranty1.setCoveredComponents(List.of("Motor", "Impeller", "Casing", "Seals", "Electronics"));
        warrantyDatabase.put("XM2000-2024-001", warranty1);

        WarrantyInfo warranty2 = new WarrantyInfo();
        warranty2.setSerialNumber("CB500-2023-045");
        warranty2.setProductModel("CB-500");
        warranty2.setCustomerId("CUST-67890");
        warranty2.setPurchaseDate(LocalDate.of(2023, 3, 10));
        warranty2.setWarrantyExpiry(LocalDate.of(2024, 3, 10));
        warranty2.setWarrantyType("Standard 1-Year");
        warranty2.setCoveredComponents(List.of("Motor", "Belt", "Rollers", "Control System"));
        warrantyDatabase.put("CB500-2023-045", warranty2);

        WarrantyInfo warranty3 = new WarrantyInfo();
        warranty3.setSerialNumber("HP1000-2024-012");
        warranty3.setProductModel("HP-1000");
        warranty3.setCustomerId("CUST-54321");
        warranty3.setPurchaseDate(LocalDate.of(2024, 8, 1));
        warranty3.setWarrantyExpiry(LocalDate.of(2027, 8, 1));
        warranty3.setWarrantyType("Extended 3-Year");
        warranty3.setCoveredComponents(List.of("Hydraulic System", "PLC", "HMI", "Safety Systems", "Structural Components"));
        warrantyDatabase.put("HP1000-2024-012", warranty3);
    }
}