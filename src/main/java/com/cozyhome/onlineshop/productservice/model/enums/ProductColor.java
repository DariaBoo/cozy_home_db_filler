package com.cozyhome.onlineshop.productservice.model.enums;

public enum ProductColor {
    BLACK("black"), ORANGE("orange"), GRAY("gray");
private String description;
    
ProductColor(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static ProductColor getByDescription(String description) {
        for (ProductColor color : ProductColor.values()){
            if(color.getDescription().equals(description)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid color description: " + description);
    }
}
