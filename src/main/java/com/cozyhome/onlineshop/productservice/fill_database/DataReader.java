package com.cozyhome.onlineshop.productservice.fill_database;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.dto.ImageDto;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.CollectionRepository;
import com.cozyhome.onlineshop.productservice.repository.ColorRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageCategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageProductRepository;
import com.cozyhome.onlineshop.productservice.repository.MaterialRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataReader {
	private final DataMapper dataMapper;

	public String readFromExcel(int rowIndex, int columnIndex) {
		String path = "products.xlsx";

		try (InputStream input = DataBuilder.class.getClassLoader().getResourceAsStream(path);
				Workbook workbook = WorkbookFactory.create(input)) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				CellType cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					return dataMapper.mapToString(cell.getStringCellValue());
				} else if (cellType == CellType.NUMERIC) {
					return dataMapper.mapToString(cell.getNumericCellValue());
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return new String();
	}
	

	public ImageDto readImagePaths(int rowIndex, int indexStart) {
		ImageDto imagePath = ImageDto.builder().popUpImageName(readFromExcel(rowIndex, indexStart++))
				.desktopImageName(readFromExcel(rowIndex, indexStart++))
				.previewImageName(readFromExcel(rowIndex, indexStart++))
				.mobileImageName(readFromExcel(rowIndex, indexStart++))
				.sliderImageName(readFromExcel(rowIndex, indexStart++)).build();
		return imagePath;
	}
}
