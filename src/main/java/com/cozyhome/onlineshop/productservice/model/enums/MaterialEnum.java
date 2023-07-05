package com.cozyhome.onlineshop.productservice.model.enums;

public enum MaterialEnum {
 TEXTILE("textile"), METAL("metal"), VELOURS("velours");
    private String description;
    
    MaterialEnum(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static MaterialEnum getByDescription(String description) {
        for (MaterialEnum material : MaterialEnum.values()){
            if(material.getDescription().equals(description)) {
                return material;
            }
        }
        throw new IllegalArgumentException("Invalid product material description: " + description);
    }
}
