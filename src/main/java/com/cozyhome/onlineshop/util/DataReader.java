package com.cozyhome.onlineshop.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.dto.ProductMeasurementsDto;
import com.cozyhome.onlineshop.service.MongoBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataReader {
	private final DataMapper mapper;
	
	@Value("${excel.file.path}")
	private String path;
	
	
	public String readFromExcel(int rowIndex, int columnIndex) {

		try (InputStream input = MongoBuilder.class.getClassLoader().getResourceAsStream(path);
				Workbook workbook = WorkbookFactory.create(input)) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				CellType cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					return mapper.mapToString(cell.getStringCellValue());
				} else if (cellType == CellType.NUMERIC) {
					return mapper.mapToString(cell.getNumericCellValue());
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

	public ProductMeasurementsDto readProductMeasurements(int rowIndex) {
		ProductMeasurementsDto dto = ProductMeasurementsDto.builder()
				.weight(readFromExcel(rowIndex, CellIndex.PRODUCT_WEIGHT))
				.height(readFromExcel(rowIndex, CellIndex.PRODUCT_HEIGHT))
				.width(readFromExcel(rowIndex, CellIndex.PRODUCT_WIDTH))
				.depth(readFromExcel(rowIndex, CellIndex.PRODUCT_DEPTH))
				.bedLength(readFromExcel(rowIndex, CellIndex.PRODUCT_BED_LENGHT))
				.bedWidth(readFromExcel(rowIndex, CellIndex.PRODUCT_BED_WIDTH))
				.build();
		return dto;
	}
	
	public int findRowIndexByValue(String targetValue, int columnIndex) {
	    try (InputStream input = MongoBuilder.class.getClassLoader().getResourceAsStream(path);
	            Workbook workbook = WorkbookFactory.create(input)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	            Row row = sheet.getRow(rowIndex);
	            if (row != null) {
	                Cell cell = row.getCell(columnIndex);
	                if (cell != null && cell.getCellType() == CellType.STRING) {
	                    String cellValue = cell.getStringCellValue();
	                    if (cellValue.equals(targetValue)) {
	                        return rowIndex;
	                    }
	                }
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return -1; 
	}
}
