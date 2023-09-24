package com.cozyhome.onlineshop.productservice.fill_database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.model.Collection;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.model.Role;
import com.cozyhome.onlineshop.productservice.model.User;
import com.cozyhome.onlineshop.productservice.repository.CollectionRepository;
import com.cozyhome.onlineshop.productservice.repository.ColorRepository;
import com.cozyhome.onlineshop.productservice.repository.MaterialRepository;
import com.cozyhome.onlineshop.productservice.repository.RoleRepository;
import com.cozyhome.onlineshop.productservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInserter {
	private final ColorRepository colorRepo;
	private final MaterialRepository materialRepo;
	private final CollectionRepository collectionRepo;
	private final RoleRepository roleRepo;
	private final UserRepository userRepo;

	private Map<String, String> colors = new HashMap<>();
	private List<String> collections = new ArrayList<>();
	private List<String> materials = new ArrayList<>();
	private List<String> roles = new ArrayList<>();
	private List<User> users = new ArrayList<>();

	{
		colors.put("#545454", "Сірий");
		colors.put("#262626", "Чорний");
		colors.put("#C57100", "Коричневий");

		collections.add("future");
		collections.add("tenderness");
		collections.add("freedom");
		collections.add("soft");
		collections.add("kasper");
		collections.add("business");
		collections.add("think");

		materials.add("Текстиль");
		materials.add("Метал");
		materials.add("Велюр");
		materials.add("Дерево");
		materials.add("Шкіра");

		roles.add(Role.UserRole.ADMIN.getDescription());
		roles.add(Role.UserRole.MANAGER.getDescription());
		roles.add(Role.UserRole.CUSTOMER.getDescription());

		users.add(User.builder().username("admin").firstName("admin").lastName("admin").email("admin@gmail.com")
				.password("admin").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().username("manager").firstName("manager").lastName("manager").email("manager@gmail.com")
				.password("manager").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().username("customer").firstName("customer").lastName("customer").email("customer@gmail.com")
				.password("customer").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
	}

	public void insertColors() {
		for (Map.Entry<String, String> entry : colors.entrySet()) {
			String hex = entry.getKey();
			String name = entry.getValue();
			Color colorToSave = Color.builder().id(hex).name(name).active(true).build();

			colorRepo.save(colorToSave);
			log.info("Color with hex: " + colorToSave.getId() + " is created!");
		}
	}

	public void insertMaterials() {
		for (String material : materials) {
			Material materialToSave = Material.builder().name(material).active(true).build();
			materialRepo.save(materialToSave);
			log.info("Material with name: " + material + " is created!");
		}
	}

	public void insertCollection() {
		for (String collection : collections) {
			Collection collectionToSave = Collection.builder().name(collection).active(true).build();
			collectionRepo.save(collectionToSave);
			log.info("Collection with name: " + collection + " is created!");
		}
	}

	public void insertRoles() {
		for (String role : roles) {
			Role roleToSave = new Role();
			roleToSave.setRole(role);
			roleRepo.save(roleToSave);
			log.info("Role: " + role + " is created!");
		}
	}
	
	public void insertUsers() {
		for(User user : users) {
			user.setRole(roleRepo.getByRole(user.getUsername()));
			userRepo.save(user);
			log.info("User with username: " + user.getUsername() + " is created!");
		}
	}
}
