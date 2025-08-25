package com.manufacturing.chatbot.manufacturing_chatbot;

import com.manufacturing.chatbot.manufacturing_chatbot.ActionRequest;
import com.manufacturing.chatbot.manufacturing_chatbot.ActionResponse;
import com.manufacturing.chatbot.manufacturing_chatbot.ProductService;
import com.manufacturing.chatbot.manufacturing_chatbot.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WarrantyService warrantyService;

    public ActionResponse handleAction(ActionRequest request) {
        try {
            switch (request.getApiPath()) {
                case "/product-specs":
                    return handleProductSpecs(request);
                case "/warranty-check":
                    return handleWarrantyCheck(request);
                default:
                    return ActionResponse.error("Unknown action: " + request.getApiPath());
            }
        } catch (Exception e) {
            return ActionResponse.error("Error processing action: " + e.getMessage());
        }
    }

    private ActionResponse handleProductSpecs(ActionRequest request) {
        String productModel = request.getParameter("productModel");
        String specificationType = request.getParameter("specificationType");
        
        if (productModel == null || productModel.trim().isEmpty()) {
            return ActionResponse.error("Product model is required");
        }

        try {
            Object specs = productService.getProductSpecifications(productModel, specificationType);
            return ActionResponse.success(specs, "Product specifications retrieved successfully");
        } catch (Exception e) {
            return ActionResponse.error("Failed to retrieve product specifications: " + e.getMessage());
        }
    }

    private ActionResponse handleWarrantyCheck(ActionRequest request) {
        String serialNumber = request.getParameter("serialNumber");
        String customerId = request.getParameter("customerId");
        
        if (serialNumber == null || serialNumber.trim().isEmpty()) {
            return ActionResponse.error("Serial number is required");
        }

        try {
            Object warranty = warrantyService.checkWarrantyStatus(serialNumber, customerId);
            return ActionResponse.success(warranty, "Warranty status retrieved successfully");
        } catch (Exception e) {
            return ActionResponse.error("Failed to check warranty status: " + e.getMessage());
        }
    }
}