package com.cozyhome.onlineshop.model;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = { "password" })
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "User")
public class User {

	@Id
	private String id;

	@UniqueElements
	private String email;
	
	@JsonIgnore
	private String password;

	private String firstName;

	private String lastName;
	
	@UniqueElements
	private String phoneNumber;

	@DBRef
	private Role role;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	
	private UserStatus status;
	
	public enum UserStatus{
		ACTIVE,
		BLOCKED,
		DELETED;
	}
	
}
