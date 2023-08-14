package com.cozyhome.onlineshop.productservice.fill_database;

import com.cozyhome.onlineshop.productservice.dto.ImageDto;
import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Collection;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.ImageCategory;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.CollectionRepository;
import com.cozyhome.onlineshop.productservice.repository.ColorRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageCategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageProductRepository;
import com.cozyhome.onlineshop.productservice.repository.MaterialRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataBuilder {
	private final CategoryRepository categoryRepo;
	private final ImageCategoryRepository imageCategoryRepo;
	private final ImageProductRepository imageProductRepo;
	private final ProductRepository productRepo;
	private final ColorRepository colorRepo;
	private final MaterialRepository materialRepo;
	private final CollectionRepository collectionRepo;	

	private Map<String, String> colors = new HashMap<>();
	private List<String> collections = new ArrayList<>();
	private List<String> materials = new ArrayList<>();

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

		int rowIndex = 2;

		ObjectId categoryId = null;
		String checkName = "";
		String categoryName = "";
		while (!(categoryName = readFromExcel(rowIndex, CellIndex.CATEGORY_NAME)).isEmpty()) {
			log.info("2 STEP");
			if (!categoryName.equals(checkName)) {
				log.info("2.1 STEP");
				checkName = categoryName;

				categoryId = buildCategory(categoryName, rowIndex);
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

	private void insertColors() {
		for (Map.Entry<String, String> entry : colors.entrySet()) {
			String hex = entry.getKey();
			String name = entry.getValue();
			Color colorToSave = Color.builder().id(hex).name(name).active(true).build();

			colorRepo.save(colorToSave);
			log.info("Color with hex: " + colorToSave.getId() + " is created!");
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

	private ObjectId buildCategory(String name, int rowIndex) {
		final boolean isCatalog = true;
		final boolean isNotCatalog = false;

		Category category = new Category();
		category.setName(name);
		category.setActive(true);
		category.setSpriteIcon(readFromExcel(rowIndex, CellIndex.CATEGORY_SVG));

		Category savedCategory = categoryRepo.save(category);
		log.info("Categoty with name: " + category.getName() + " is created!");

		String imageNameCatalog = readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_CATALOG);
		if (!imageNameCatalog.isEmpty()) {
			doBuildCategoryImage(imageNameCatalog, savedCategory, isCatalog);
		}

		String imageNameHomePage = readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_HOMEPAGE);
		String imageSizeHomePage = readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_HOMEPAGE_SIZE);
		if (!imageNameHomePage.isEmpty() && !imageSizeHomePage.isEmpty()) {
			doBuildCategoryImage(imageNameHomePage, savedCategory, isNotCatalog, imageSizeHomePage);
		}
		return savedCategory.getId();
	}

	private void doBuildCategoryImage(String imageName, Category categry, boolean isCatalog) {
		ImageCategory image = ImageCategory.builder().categoryImageName(imageName).catalog(isCatalog).category(categry)
				.build();
		imageCategoryRepo.save(image);
		log.info("Image with path: " + image.getCategoryImageName() + " is created!");
	}

	private void doBuildCategoryImage(String imageName, Category categry, boolean isCatalog, String imageSize) {
		ImageCategory image = ImageCategory.builder().categoryImageName(imageName).imageSize(imageSize)
				.catalog(isCatalog).category(categry).build();
		imageCategoryRepo.save(image);
		log.info("Image with path: " + image.getCategoryImageName() + " is created!");
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
				.subCategory(categoryRepo.getCategoryById(categoryId)).createdAt(LocalDateTime.now())
				.averageRating(new Random().nextInt(6)).popularRating((byte) new Random().nextInt(6))
				.materials(buildMaterialsList(rowIndex))
				.weight(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WEIGHT)))
				.height(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_HEIGHT)))
				.width(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WIDTH)))
				.depth(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_DEPTH))).build();

		String result = productRepo.save(addAdditionalCharacteristics(product, rowIndex)).getSkuCode();
		log.info("Product with name: " + product.getName() + " is created!");
		return result;
	}

	private Product addAdditionalCharacteristics(Product product, int rowIndex) {
		String transformation = readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if (!transformation.isEmpty()) {
			boolean isTransformation = mapToBoolean(transformation);
			product.setTransformation(isTransformation);
		}
		String heightRegulation = readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if (!heightRegulation.isEmpty()) {
			boolean isHeightRegulation = mapToBoolean(heightRegulation);
			product.setHeightRegulation(isHeightRegulation);
		}
		String numberOfDoors = readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DOORS);
		if (!numberOfDoors.isEmpty()) {
			byte numberOfDoorsByte = mapToByte(numberOfDoors);
			product.setNumberOfDoors(numberOfDoorsByte);
		}
		String numberOfDrawers = readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DRAWERS);
		if (!numberOfDrawers.isEmpty()) {
			byte numberOfDrawersByte = mapToByte(numberOfDrawers);
			product.setNumberOfDrawers(numberOfDrawersByte);
		}
		String bedLength = readFromExcel(rowIndex, CellIndex.PRODUCT_BED_LENGHT);
		if (!bedLength.isEmpty()) {
			float bedLengthFloat = mapToFloat(bedLength);
			product.setBedLength(bedLengthFloat);
		}
		String bedWidth = readFromExcel(rowIndex, CellIndex.PRODUCT_BED_WIDTH);
		if (!bedWidth.isEmpty()) {
			float bedWidthFloat = mapToFloat(bedWidth);
			product.setBedWidth(bedWidthFloat);
		}
		String maxLoad = readFromExcel(rowIndex, CellIndex.PRODUCT_MAX_LOAD);
		if (!maxLoad.isEmpty()) {
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
		int IMAGE_START_INDEX_COLOR1 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR1;
		int IMAGE_START_INDEX_COLOR2 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR2;
		int IMAGE_START_INDEX_COLOR3 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR3;
		final int countOfUniqueImages = 4;
		final int nextStartIndex = 5;//count of image paths for every unique image

		String color1 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_1).trim();
		String color2 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_2).trim();
		String color3 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_3).trim();

		ImageDto imagePath;
		if (!color1.isEmpty()) {			
			for (int i = 0; i < countOfUniqueImages; i++) {
				imagePath = readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR1);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color1, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR1 += nextStartIndex;
			}
		}

		if (!color2.isEmpty()) {
			for (int i = 0; i < countOfUniqueImages; i++) {
				imagePath = readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR2);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color2, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR2 += nextStartIndex;
			}
		}

		if (!color3.isEmpty()) {
			for (int i = 0; i < countOfUniqueImages; i++) {				
				imagePath = readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR3);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color3, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR3 += nextStartIndex;
			}
		}
	}

	private ImageDto readImagePaths(int rowIndex, int indexStart) {
		ImageDto imagePath = ImageDto.builder().popUpImageName(readFromExcel(rowIndex, indexStart++))
				.desktopImageName(readFromExcel(rowIndex, indexStart++))
				.previewImageName(readFromExcel(rowIndex, indexStart++))
				.mobileImageName(readFromExcel(rowIndex, indexStart++))
				.sliderImageName(readFromExcel(rowIndex, indexStart++)).build();
		return imagePath;
	}

	private void doBuildProductImage(String color, String productSkuCode, ImageDto imageDto, boolean isMain) {
		Color colorToSave = colorRepo.getByName(color);
		Product productToSave = productRepo.getProductBySkuCode(productSkuCode);
		if (colorToSave != null && productToSave != null) {
			ImageProduct imageToSave = ImageProduct.builder().color(colorToSave).product(productToSave)
					.popUpImageName(imageDto.getPopUpImageName()).desktopImageName(imageDto.getDesktopImageName())
					.previewImageName(imageDto.getPreviewImageName()).mobileImageName(imageDto.getMobileImageName())
					.sliderImageName(imageDto.getSliderImageName()).mainPhoto(isMain).build();
			imageProductRepo.save(imageToSave);
			log.info("Image with path: " + imageDto.getPreviewImageName() + " is created!");
		}
	}

	private short mapToShort(String value) {
		if (!value.isEmpty()) {
			return Short.parseShort(value);
		}
		return 0;
	}

	private Float mapToFloat(String value) {
		if (!value.isEmpty()) {
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
