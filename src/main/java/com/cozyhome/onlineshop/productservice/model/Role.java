package com.cozyhome.onlineshop.productservice.model;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Role")
public class Role {
	
	@Id
    private String id;
	@UniqueElements
    private String role;
	
	public enum UserRole{
		ADMIN("admin"),
		CUSTOMER("customer"),
		MANAGER("manager");
    	
    	private String description;
    	
    	private UserRole(String description) {
    		this.description = description;
    	}
    	
    	public String getDescription() {
    		return this.description;
    	}
	}
    
}
