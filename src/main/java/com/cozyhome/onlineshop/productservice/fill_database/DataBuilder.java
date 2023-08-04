package com.cozyhome.onlineshop.productservice.fill_database;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Collection;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.Image;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.CollectionRepository;
import com.cozyhome.onlineshop.productservice.repository.ColorRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageRepository;
import com.cozyhome.onlineshop.productservice.repository.MaterialRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataBuilder {
	private final CategoryRepository categoryRepo;
	private final ImageRepository imageRepo;
	private final ProductRepository productRepo;
	private final ColorRepository colorRepo;
	private final MaterialRepository materialRepo;
	private final CollectionRepository collectionRepo;

	private List<String> colors = new ArrayList<>();
	private Map<String, String> hex = new HashMap<>();
	private List<String> collections = new ArrayList<>();
	private List<String> materials = new ArrayList<>();

	{
		colors.add("Сірий");
		colors.add("Чорний");
		colors.add("Коричневий");

		hex.put("Сірий", "#545454");
		hex.put("Чорний", "#291D0B");
		hex.put("Коричневий", "#D99616");

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
	}

	public void insertData() {
		log.info("0 STEP");
		insertColors();
		insertMaterials();
		insertCollection();

		buildData();
	}

	private void buildData() {
		log.info("1 STEP");

		int rowIndex = 1;

		ObjectId categoryId = null;
		String checkName = "";
		String categoryName = "";
		while (!(categoryName = readFromExcel(rowIndex, CellIndex.CATEGORY_NAME)).isEmpty()) {
			log.info("2 STEP");
			if (!categoryName.equals(checkName)) {
				log.info("2.1 STEP");
				checkName = categoryName;
				String categoryImageName = readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_PATH);
				categoryId = buildCategory(categoryName, categoryImageName, rowIndex);
			}

			String subcategoryName = "";
			while (!(subcategoryName = readFromExcel(rowIndex, CellIndex.SUBCATEGORY_NAME)).isEmpty()) {
				log.info("3 STEP");
				ObjectId subcategoryId = buildSubcategory(subcategoryName, categoryId);

				while (!readFromExcel(rowIndex, CellIndex.PRODUCT_NAME).isEmpty()) {
					log.info("4 STEP");
					String productId = buildProduct(rowIndex, subcategoryId);
					buildImages(rowIndex, productId);
					rowIndex++;
				}
			}
			rowIndex++;
		}
	}

	public void insertColors() {
		for (String color : colors) {
			Color colorToSave = Color.builder()
					.id(hex.get(color))
					.name(color).active(true).build();

			colorRepo.save(colorToSave);
			log.info("Color with name: " + color + " is created!");
		}
	}

	private void insertMaterials() {
		for (String material : materials) {
			Material materialToSave = Material.builder().name(material).active(true).build();
			materialRepo.save(materialToSave);
			log.info("Material with name: " + material + " is created!");
		}
	}

	private void insertCollection() {
		for (String collection : collections) {
			Collection collectionToSave = Collection.builder().name(collection).active(true).build();
			collectionRepo.save(collectionToSave);
			log.info("Collection with name: " + collection + " is created!");
		}
	}

	private ObjectId buildCategory(String name, String imageName, int rowIndex) {
		Category category = new Category();
		category.setName(name);
		category.setActive(true);
		category.setCategoryImageName(imageName);
		category.setSpriteIcon(readFromExcel(rowIndex, CellIndex.CATEGORY_SVG));

		ObjectId categoryId = categoryRepo.save(category).getId();
		log.info("Categoty with name: " + category.getName() + " is created!");

		return categoryId;
	}

	private ObjectId buildSubcategory(String name, ObjectId categoryId) {
		Category subCategory = new Category();
		subCategory.setName(name);
		subCategory.setActive(true);
		subCategory.setParentId(categoryId);

		ObjectId subcategoryId = categoryRepo.save(subCategory).getId();
		log.info("Subcategory with name: " + subCategory.getName() + " is created!");

		return subcategoryId;
	}

	private String buildProduct(int rowIndex, ObjectId categoryId) {

		Product product = Product.builder().skuCode(readFromExcel(rowIndex, CellIndex.PRODUCT_SKU))
				.name(readFromExcel(rowIndex, CellIndex.PRODUCT_NAME))
				.shortDescription(readFromExcel(rowIndex, CellIndex.PRODUCT_SHORT_DESCRIPTION))
				.description(readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION))
				.price(new BigDecimal(readFromExcel(rowIndex, CellIndex.PRODUCT_PRICE)))
				.discount(mapToByte(readFromExcel(rowIndex, CellIndex.PRODUCT_DISCOUNT)))
				.status(ProductStatus.values()[new Random().nextInt(3)])
				.collection(collectionRepo
						.getByName(readFromExcel(rowIndex, CellIndex.PRODUCT_COLLECTION).toLowerCase().trim()))
				.categoryId(categoryId).createdAt(LocalDateTime.now()).averageRating(5)
				.materials(buildMaterialsList(rowIndex))
				.weight(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WEIGHT)))
				.height(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_HEIGHT)))
				.width(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WIDTH)))
				.depth(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_DEPTH)))
				.build();

		String result = productRepo.save(addAdditionalCharacteristics(product, rowIndex)).getSkuCode();
		log.info("Product with name: " + product.getName() + " is created!");
		return result;
	}

	private Product addAdditionalCharacteristics(Product product, int rowIndex) {
		String transformation = readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if(!transformation.isEmpty()) {
			boolean isTransformation = mapToBoolean(transformation);
			product.setTransformation(isTransformation);
		}
		String heightRegulation = readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if(!heightRegulation.isEmpty()) {
			boolean isHeightRegulation = mapToBoolean(heightRegulation);
			product.setHeightRegulation(isHeightRegulation);
		}
		String numberOfDoors = readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DOORS);
		if(!numberOfDoors.isEmpty()) {
			byte numberOfDoorsByte = mapToByte(numberOfDoors);
			product.setNumberOfDoors(numberOfDoorsByte);
		}
		String numberOfDrawers = readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DRAWERS);
		if(!numberOfDrawers.isEmpty()) {
			byte numberOfDrawersByte = mapToByte(numberOfDrawers);
			product.setNumberOfDrawers(numberOfDrawersByte);
		}
		String bedLength = readFromExcel(rowIndex, CellIndex.PRODUCT_BED_LENGHT);
		if(!bedLength.isEmpty()) {
			float bedLengthFloat = mapToFloat(bedLength);
			product.setBedLength(bedLengthFloat);
		}
		String bedWidth = readFromExcel(rowIndex, CellIndex.PRODUCT_BED_WIDTH);
		if(!bedWidth.isEmpty()) {
			float bedWidthFloat = mapToFloat(bedWidth);
			product.setBedWidth(bedWidthFloat);
		}
		String maxLoad = readFromExcel(rowIndex, CellIndex.PRODUCT_MAX_LOAD);
		if(!maxLoad.isEmpty()) {
			product.setMaxLoad(mapToShort(maxLoad));
		}
		return product;
	}

	private List<Material> buildMaterialsList(int rowIndex) {
		String material1 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_1);
		String material2 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_2);
		String material3 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_3);
		List<Material> materials = new ArrayList<>();
		if (!material1.isEmpty()) {
			materials.add(materialRepo.getByName(material1.trim()));
		}
		if (!material2.isEmpty()) {
			materials.add(materialRepo.getByName(material2.trim()));
		}
		if (!material3.isEmpty()) {
			materials.add(materialRepo.getByName(material3.trim()));
		}
		return materials;
	}

	private void buildImages(int rowIndex, String productSkuCode) {
		String color1 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_1).trim();
		String color2 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_2).trim();
		String color3 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_3).trim();

		System.out.println(color1);
		System.out.println(color2);
		System.out.println(color3);

		if (!color1.isEmpty()) {
			String imagePath1 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_1_1);
			if (!imagePath1.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath1).preview(true).color(colorRepo.getByName(color1))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath2 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_1_2);
			if (!imagePath2.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath2).preview(true).color(colorRepo.getByName(color1))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath3 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_1_3);
			if (!imagePath3.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath3).preview(true).color(colorRepo.getByName(color1))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
		}

		if (!color2.isEmpty()) {
			String imagePath1 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_2_1);
			if (!imagePath1.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath1).preview(true).color(colorRepo.getByName(color2))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath2 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_2_2);
			if (!imagePath2.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath2).preview(true).color(colorRepo.getByName(color2))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath3 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_2_3);
			if (!imagePath3.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath3).preview(true).color(colorRepo.getByName(color2))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
		}

		if (!color3.isEmpty()) {
			String imagePath1 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_3_1);
			if (!imagePath1.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath1).preview(true).color(colorRepo.getByName(color3))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath2 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_3_2);
			if (!imagePath2.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath2).preview(true).color(colorRepo.getByName(color3))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
			String imagePath3 = readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_3_3);
			if (!imagePath3.isEmpty()) {
				Image image = Image.builder().imagePath(imagePath3).preview(true).color(colorRepo.getByName(color3))
						.productSkuCode(productSkuCode).build();
				imageRepo.save(image);
				log.info("Image with path: " + image.getImagePath() + " is created!");
			}
		}
	}

	private short mapToShort(String value) {
		if(!value.isEmpty()) {
			return Short.parseShort(value);
		}
		return 0;
	}


	private Float mapToFloat(String value) {
		if(!value.isEmpty()) {
			return Float.parseFloat(value);
		}
		return null;
	}

	private String mapToString(Object value) {
		return String.valueOf(value);
	}

	private byte mapToByte(String value) {
		int x = 0;
		if (!mapToString(value).isEmpty()) {
			x = Integer.valueOf(value);
		}
		return (byte) x;
	}

	private boolean mapToBoolean(String value) {
		return Boolean.parseBoolean(value);
	}

	private String readFromExcel(int rowIndex, int columnIndex) {
		String path = "products.xlsx";

		try (InputStream input = DataBuilder.class.getClassLoader().getResourceAsStream(path);
			 Workbook workbook = WorkbookFactory.create(input)) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				CellType cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					return mapToString(cell.getStringCellValue());
				} else if (cellType == CellType.NUMERIC) {
					return mapToString(cell.getNumericCellValue());
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return new String();
	}
}
