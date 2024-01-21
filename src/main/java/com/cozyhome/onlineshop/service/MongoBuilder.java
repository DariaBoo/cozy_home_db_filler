package com.cozyhome.onlineshop.service;

import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_17;
import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_38;
import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_59;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.model.Category;
import com.cozyhome.onlineshop.model.Color;
import com.cozyhome.onlineshop.model.ImageCategory;
import com.cozyhome.onlineshop.model.ImageProduct;
import com.cozyhome.onlineshop.model.Material;
import com.cozyhome.onlineshop.model.Product;
import com.cozyhome.onlineshop.model.enums.ProductStatus;
import com.cozyhome.onlineshop.repository.CategoryRepository;
import com.cozyhome.onlineshop.repository.CollectionRepository;
import com.cozyhome.onlineshop.repository.ColorRepository;
import com.cozyhome.onlineshop.repository.ImageCategoryRepository;
import com.cozyhome.onlineshop.repository.ImageProductRepository;
import com.cozyhome.onlineshop.repository.MaterialRepository;
import com.cozyhome.onlineshop.repository.ProductRepository;
import com.cozyhome.onlineshop.util.CellIndex;
import com.cozyhome.onlineshop.util.DataMapper;
import com.cozyhome.onlineshop.util.DataReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoBuilder {
	private final CategoryRepository categoryRepo;
	private final ImageCategoryRepository imageCategoryRepo;
	private final ImageProductRepository imageProductRepo;
	private final ProductRepository productRepo;
	private final ColorRepository colorRepo;
	private final MaterialRepository materialRepo;
	private final CollectionRepository collectionRepo;
	private final DataReader reader;
	private final DataMapper mapper;

	private final Map<Integer, Integer> colorsQuantityConstants = new HashMap<>();

	{
		colorsQuantityConstants.put(0, PRODUCT_QUANTITY_FOR_COLOR_17);
		colorsQuantityConstants.put(1, PRODUCT_QUANTITY_FOR_COLOR_38);
		colorsQuantityConstants.put(2, PRODUCT_QUANTITY_FOR_COLOR_59);
	}
	
	public void buildData() {
		log.info("1 STEP");

		int rowIndex = 2;

		ObjectId categoryId = null;
		String checkName = "";
		String categoryName = "";
		while (!(categoryName = reader.readFromExcel(rowIndex, CellIndex.CATEGORY_NAME)).isEmpty()) {
			log.info("2 STEP");
			if (!categoryName.equals(checkName)) {
				log.info("2.1 STEP");
				checkName = categoryName;

				categoryId = buildCategory(categoryName, rowIndex);
			}

			String subcategoryName = "";
			while (!(subcategoryName = reader.readFromExcel(rowIndex, CellIndex.SUBCATEGORY_NAME)).isEmpty()) {
				log.info("3 STEP");
				ObjectId subcategoryId = buildSubcategory(subcategoryName, categoryId);

				while (!reader.readFromExcel(rowIndex, CellIndex.PRODUCT_NAME).isEmpty()) {
					log.info("4 STEP");
					String productId = buildProduct(rowIndex, subcategoryId);
					buildImages(rowIndex, productId);
					rowIndex++;
				}
			}
			rowIndex++;
		}
	}	

	private ObjectId buildCategory(String name, int rowIndex) {
		final boolean isCatalog = true;
		final boolean isNotCatalog = false;

		Category category = new Category();
		category.setName(name);
		category.setActive(true);
		category.setSpriteIcon(reader.readFromExcel(rowIndex, CellIndex.CATEGORY_SVG));

		Category savedCategory = categoryRepo.save(category);
		log.info("Categoty with name: " + category.getName() + " is created!");

		String imageNameCatalog = reader.readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_CATALOG);
		if (!imageNameCatalog.isEmpty()) {
			doBuildCategoryImage(imageNameCatalog, savedCategory, isCatalog);
		}

		String imageNameHomePage = reader.readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_HOMEPAGE);
		String imageSizeHomePage = reader.readFromExcel(rowIndex, CellIndex.CATEGORY_IMAGE_HOMEPAGE_SIZE);
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
		BigDecimal price = new BigDecimal(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_PRICE));
		String discount = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DISCOUNT);
		
		Product product = Product.builder().skuCode(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_SKU))
				.name(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_NAME))
				.shortDescription(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_SHORT_DESCRIPTION))
				.description(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION))
				.price(price)
				.discount(mapper.mapToByte(discount))
				.priceWithDiscount(calculatePriceWithDiscount(price, discount))
				.status(ProductStatus.values()[new Random().nextInt(3)])
				.collection(collectionRepo
						.getByName(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_COLLECTION).toLowerCase().trim()))
				.subCategory(categoryRepo.getCategoryById(categoryId)).createdAt(LocalDateTime.now())
				.averageRating(new Random().nextFloat(6))
				.popularRating((byte) new Random().nextInt(6))
				.materials(buildMaterialsList(rowIndex))
				.weight(mapper.mapToFloat(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_WEIGHT)))
				.height(mapper.mapToFloat(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_HEIGHT)))
				.width(mapper.mapToFloat(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_WIDTH)))
				.depth(mapper.mapToFloat(reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DEPTH)))
				.available(checkAvailable(rowIndex))
				.build();

		String result = productRepo.save(addAdditionalCharacteristics(product, rowIndex)).getSkuCode();
		log.info("Product with skuCode: " + product.getSkuCode() + " is created!");
		return result;
	}
	
	public BigDecimal calculatePriceWithDiscount(BigDecimal price, String discount) {
	    if (discount.isEmpty()) {
	      return price;
	    }
	    return price.subtract((price.multiply(new BigDecimal(discount))).divide(new BigDecimal(100)));
	  }

	private boolean checkAvailable(int rowIndex) {
		boolean available = false;
		int count = 0;

		for (int i = 0; i < 3; i++) {
			int quantity = 0;
			String quantityString = reader.readFromExcel(rowIndex, colorsQuantityConstants.get(i)).trim();
			quantity = convertToInt(quantityString);

			if (!quantityString.isEmpty()) {
				quantity = Math.round(Float.parseFloat(quantityString));
			}
			count = count + quantity;
		}

		if (count > 0) {
			available = true;
		}
		return available;
	}

	private int convertToInt(String quantityString) {
		int quantity = 0;
		if (!quantityString.isEmpty()) {
			quantity = Math.round(Float.parseFloat(quantityString));
		}
		return quantity;
	}

	private Product addAdditionalCharacteristics(Product product, int rowIndex) {
		String transformation = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if (!transformation.isEmpty()) {
			boolean isTransformation = mapper.mapToBoolean(transformation);
			product.setTransformation(isTransformation);
		}
		String heightRegulation = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_TRANSFORMATION);
		if (!heightRegulation.isEmpty()) {
			boolean isHeightRegulation = mapper.mapToBoolean(heightRegulation);
			product.setHeightRegulation(isHeightRegulation);
		}
		String numberOfDoors = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DOORS);
		if (!numberOfDoors.isEmpty()) {
			byte numberOfDoorsByte = mapper.mapToByte(numberOfDoors);
			product.setNumberOfDoors(numberOfDoorsByte);
		}
		String numberOfDrawers = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_NUMBER_OF_DRAWERS);
		if (!numberOfDrawers.isEmpty()) {
			byte numberOfDrawersByte = mapper.mapToByte(numberOfDrawers);
			product.setNumberOfDrawers(numberOfDrawersByte);
		}
		String bedLength = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_BED_LENGHT);
		if (!bedLength.isEmpty()) {
			float bedLengthFloat = mapper.mapToFloat(bedLength);
			product.setBedLength(bedLengthFloat);
		}
		String bedWidth = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_BED_WIDTH);
		if (!bedWidth.isEmpty()) {
			float bedWidthFloat = mapper.mapToFloat(bedWidth);
			product.setBedWidth(bedWidthFloat);
		}
		String maxLoad = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_MAX_LOAD);
		if (!maxLoad.isEmpty()) {
			product.setMaxLoad(mapper.mapToShort(maxLoad));
		}
		return product;
	}

	private List<Material> buildMaterialsList(int rowIndex) {
		String material1 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_1);
		String material2 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_2);
		String material3 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_3);
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

	public void buildImages(int rowIndex, String productSkuCode) {
		int IMAGE_START_INDEX_COLOR1 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR1;
		int IMAGE_START_INDEX_COLOR2 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR2;
		int IMAGE_START_INDEX_COLOR3 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR3;
		final int countOfUniqueFirstImage = 5;
		final int countOfUniqueImages = 4;
		final int nextStartIndex = 5;//count of image paths for every unique image

		String color1 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_1).trim();
		String color2 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_2).trim();
		String color3 = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_3).trim();

		ImageDto imagePath;
		if (!color1.isEmpty()) {			
			for (int i = 0; i < countOfUniqueFirstImage; i++) {
				imagePath = reader.readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR1);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color1, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR1 += nextStartIndex;
			}
		}

		if (!color2.isEmpty()) {
			for (int i = 0; i < countOfUniqueImages; i++) {
				imagePath = reader.readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR2);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color2, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR2 += nextStartIndex;
			}
		}

		if (!color3.isEmpty()) {
			for (int i = 0; i < countOfUniqueImages; i++) {				
				imagePath = reader.readImagePaths(rowIndex, IMAGE_START_INDEX_COLOR3);
				boolean isMain = i==0 ? true : false;
				if (!imagePath.getPreviewImageName().isEmpty()) {
					doBuildProductImage(color3, productSkuCode, imagePath, isMain);
				}
				IMAGE_START_INDEX_COLOR3 += nextStartIndex;
			}
		}
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
}
