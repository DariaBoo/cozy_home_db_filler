package com.cozyhome.onlineshop.fill_database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.model.Collection;
import com.cozyhome.onlineshop.model.Color;
import com.cozyhome.onlineshop.model.Material;
import com.cozyhome.onlineshop.model.Role;
import com.cozyhome.onlineshop.model.User;
import com.cozyhome.onlineshop.repository.CollectionRepository;
import com.cozyhome.onlineshop.repository.ColorRepository;
import com.cozyhome.onlineshop.repository.MaterialRepository;
import com.cozyhome.onlineshop.repository.RoleRepository;
import com.cozyhome.onlineshop.repository.UserRepository;

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
	private List<Role.RoleE> roleNames = new ArrayList<>();
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

		roleNames.add(Role.RoleE.ADMIN);
		roleNames.add(Role.RoleE.MANAGER);
		roleNames.add(Role.RoleE.CUSTOMER);

		users.add(User.builder().email("admin@gmail.com").firstName("ADMIN").lastName("admin")
				.password("admin").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("manager@gmail.com").firstName("MANAGER").lastName("manager")
				.password("manager").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer@gmail.com").firstName("CUSTOMER").lastName("customer")
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
		for (Role.RoleE name : roleNames) {
			Role roleToSave = new Role();
			roleToSave.setName(name);
			roleRepo.save(roleToSave);
			log.info("Role: " + name + " is created!");
		}
	}
	
	public void insertUsers() {
		for(User user : users) {
			Role role = roleRepo.getByName(user.getFirstName());
			user.setRoles(Set.of(role));
			userRepo.save(user);
			log.info("User with username: " + user.getEmail() + " is created!");
		}
	}
}
