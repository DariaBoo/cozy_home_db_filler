package com.cozyhome.onlineshop.productservice.model.enums;

public enum ProductStatus {
    NEW("new"), POPULAR("popular"), REGULAR("regular"), DELETED("deleted");
    private String description;
    private ProductStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static ProductStatus getStatusByDescription(String description) {
        for (ProductStatus status : ProductStatus.values()){
            if(status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid product status description: " + description);
    }
}
