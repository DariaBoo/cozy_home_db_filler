package com.cozyhome.onlineshop.model;

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
    private RoleE name;
    
    public enum RoleE{
		ADMIN,
		CUSTOMER,
		MANAGER;    	    	
	}
    
}
