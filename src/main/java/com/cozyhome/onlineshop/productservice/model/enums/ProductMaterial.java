package com.cozyhome.onlineshop.productservice.model.enums;

public enum ProductMaterial {
 TEXTILE("Текстиль"), METAL("Метал"), VELOURS("Велюр"), WOOD("Дерево"), LEATHER("Шкіра");
    private String description;
    
    ProductMaterial(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static ProductMaterial getByDescription(String description) {
        for (ProductMaterial material : ProductMaterial.values()){
            if(material.getDescription().equals(description)) {
                return material;
            }
        }
        throw new IllegalArgumentException("Invalid product material description: ." + description + ".");
    }
}
