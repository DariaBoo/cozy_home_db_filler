package com.cozyhome.onlineshop.productservice.fill_database;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.cozyhome.onlineshop.productservice.model.Image;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductCollection;
import com.cozyhome.onlineshop.productservice.model.enums.ProductColor;
import com.cozyhome.onlineshop.productservice.model.enums.ProductMaterial;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataCreator {
    private final CategoryRepository categoryRepo;
    private final ImageRepository imageRepo;
    private final ProductRepository productRepo;

    public void createCategories() {
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
                categoryId = createCategory(categoryName);
            }

            String subcategoryName = "";
            while (!(subcategoryName = readFromExcel(rowIndex, CellIndex.SUBCATEGORY_NAME)).isEmpty()) {
                log.info("3 STEP");
                ObjectId subcategoryId = createSubcategory(subcategoryName, categoryId);

                while (!readFromExcel(rowIndex, CellIndex.PRODUCT_NAME).isEmpty()) {
                    log.info("4 STEP");
                    String productId = createProduct(rowIndex, subcategoryId);
                    createImages(rowIndex, productId);
                    rowIndex++;
                }
            }
            rowIndex++;
        }
    }

    private ObjectId createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setActive(true);

        ObjectId categoryId = categoryRepo.save(category).getId();
        log.info("Categoty with name: " + category.getName() + " is created!");

        return categoryId;
    }

    private ObjectId createSubcategory(String name, ObjectId categoryId) {
        Category subCategory = new Category();
        subCategory.setName(name);
        subCategory.setActive(true);
        subCategory.setParentId(categoryId);

        ObjectId subcategoryId = categoryRepo.save(subCategory).getId();
        log.info("Subcategory with name: " + subCategory.getName() + " is created!");

        return subcategoryId;
    }

    private String createProduct(int rowIndex, ObjectId subcategoryId) {
        Product product = Product.builder().skuCode(readFromExcel(rowIndex, CellIndex.PRODUCT_SKU))
                .name(readFromExcel(rowIndex, CellIndex.PRODUCT_NAME))
                .shortDescription(readFromExcel(rowIndex, CellIndex.PRODUCT_SHORT_DESCRIPTION))
                .description(readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION))
                .price(new BigDecimal(readFromExcel(rowIndex, CellIndex.PRODUCT_PRICE)))
                .discount(mapToByte(readFromExcel(rowIndex, CellIndex.PRODUCT_DISCOUNT)))
                .status(ProductStatus.values()[new Random().nextInt(3)])
                .productCollection(ProductCollection.getCollectionByDescription(
                        readFromExcel(rowIndex, CellIndex.PRODUCT_COLLECTION).toLowerCase().trim()))
                .categoryId(subcategoryId).createdAt(LocalDateTime.now()).averageRating(5)
                .materials(createMaterialsList(rowIndex))
                .weight(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WEIGHT)))
                .height(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_HEIGHT)))
                .width(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_WIDTH)))
                .depth(mapToFloat(readFromExcel(rowIndex, CellIndex.PRODUCT_DEPTH))).build();

        String result = productRepo.save(product).getSkuCode();
        log.info("Product with name: " + product.getName() + " is created!");
        return result;
    }

    private List<ProductMaterial> createMaterialsList(int rowIndex) {
        String material1 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_1);
        String material2 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_2);
        String material3 = readFromExcel(rowIndex, CellIndex.PRODUCT_MATERIAL_3);
        List<ProductMaterial> materials = new ArrayList<>();
        if (!material1.isEmpty()) {
            materials.add(ProductMaterial.getByDescription(material1.trim()));
        }
        if (!material2.isEmpty()) {
            materials.add(ProductMaterial.getByDescription(material2.trim()));
        }
        if (!material3.isEmpty()) {
            materials.add(ProductMaterial.getByDescription(material3.trim()));
        }
        return materials;
    }

    private void createImages(int rowIndex, String productId) {
        String color1 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_1).trim();
        String color2 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_2).trim();
        String color3 = readFromExcel(rowIndex, CellIndex.PRODUCT_COLOR_3).trim();

        if (!color1.isEmpty()) {
            Image image = Image.builder().imagePath(readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_1)).preview(true)
                    .color(ProductColor.getByDescription(color1)).productId(productId).build();
            imageRepo.save(image);
            log.info("Image with path: " + image.getImagePath() + " is created!");
        }

        if (!color2.isEmpty()) {
            Image image = Image.builder().imagePath(readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_2)).preview(true)
                    .color(ProductColor.getByDescription(color2)).productId(productId).build();
            imageRepo.save(image);
        }

        if (!color3.isEmpty()) {
            Image image = Image.builder().imagePath(readFromExcel(rowIndex, CellIndex.PRODUCT_IMAGE_3)).preview(true)
                    .color(ProductColor.getByDescription(color3)).productId(productId).build();
            imageRepo.save(image);
        }
    }

    private float mapToFloat(String value) {
        return Float.parseFloat(value);
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

    private String readFromExcel(int rowIndex, int columnIndex) {
        String path = "products.xlsx";

        try (InputStream input = DataCreator.class.getClassLoader().getResourceAsStream(path);
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
