package com.cozyhome.onlineshop.fill_database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder encoder;

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

		roleNames.add(Role.RoleE.ROLE_ADMIN);
		roleNames.add(Role.RoleE.ROLE_MANAGER);
		roleNames.add(Role.RoleE.ROLE_CUSTOMER);

		users.add(User.builder()
				.email("admin@gmail.com")
				.firstName("ADMIN")
				.lastName("admin")
				.password("admiN1$2")
				.phoneNumber("+38 (123) 123 - 45 - 67")
				.status(User.UserStatus.ACTIVE)
				.createdAt(LocalDateTime.now())
				.build());
		users.add(User.builder().email("manager@gmail.com").firstName("MANAGER").lastName("manager")
				.password("manageR1$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer1.cozy.home@gmail.com").firstName("Шевченко").lastName("Микола")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());

		users.add(User.builder().email("customer1@gmail.com").firstName("Абрамчук").lastName("Юрій")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer2@gmail.com").firstName("Березюк").lastName("Аліна")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer3@gmail.com").firstName("Бойко").lastName("Яна")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer4@gmail.com").firstName("Ковальчук").lastName("Дарина")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer5@gmail.com").firstName("Ткаченко").lastName("Давід")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer6@gmail.com").firstName("Олийник").lastName("Андрій")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer7@gmail.com").firstName("Безбородко").lastName("Вероніка")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer8@gmail.com").firstName("Кравченко").lastName("Андрій")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer9@gmail.com").firstName("Зеленчук").lastName("Тимофій")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer10@gmail.com").firstName("Журавко").lastName("Мілана")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer11@gmail.com").firstName("Захарчук").lastName("Поліна")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer12@gmail.com").firstName("Кучура").lastName("Владислав")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer13@gmail.com").firstName("Васько").lastName("Вероніка")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer14@gmail.com").firstName("Грицак").lastName("Артем")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer15@gmail.com").firstName("Лесненко").lastName("Софія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer16@gmail.com").firstName("Липа").lastName("Назар")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer17@gmail.com").firstName("Ельченко").lastName("Дарья")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer18@gmail.com").firstName("Макарчук").lastName("Марія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer19@gmail.com").firstName("Нестеренко").lastName("Максим")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer20@gmail.com").firstName("Гришко").lastName("Катерина")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer21@gmail.com").firstName("Лучко").lastName("Анна")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer22@gmail.com").firstName("Прилепа").lastName("Катерина")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer23@gmail.com").firstName("Повалак").lastName("Вікторія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer24@gmail.com").firstName("Минко").lastName("Антон")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer25@gmail.com").firstName("Стрельчук").lastName("Давід")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer26@gmail.com").firstName("Струк").lastName("Анна")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer27@gmail.com").firstName("Ходченко").lastName("Меланія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer28@gmail.com").firstName("Савелюк").lastName("Віктор")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer29@gmail.com").firstName("Рыбак").lastName("Вікторія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer30@gmail.com").firstName("Табачко").lastName("Наталія")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer31@gmail.com").firstName("Черкун").lastName("Микола")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer32@gmail.com").firstName("Степаненко").lastName("Анатолій")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer33@gmail.com").firstName("Сорока").lastName("Ольга")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer34@gmail.com").firstName("Соболенко").lastName("Петро")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());
		users.add(User.builder().email("customer35@gmail.com").firstName("Родченко").lastName("Микола")
				.password("customer12W$").status(User.UserStatus.ACTIVE).createdAt(LocalDateTime.now()).build());

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
			User userToSave = User.builder()
					.email(user.getEmail())
					.firstName(user.getFirstName())
					.lastName(user.getLastName())
					.password(encoder.encode(user.getPassword()))
					.status(User.UserStatus.ACTIVE)
					.createdAt(LocalDateTime.now())
					.roles(Set.of(role))
					.build();
			userRepo.save(userToSave);
			log.info("User with username: " + user.getEmail() + " is created!");
		}
	}
}
