package com.cozyhome.onlineshop.productservice.fill_database;

import org.springframework.stereotype.Component;

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
public class DataMapper {

	public short mapToShort(String value) {
		if (!value.isEmpty()) {
			return Short.parseShort(value);
		}
		return 0;
	}

	public Float mapToFloat(String value) {
		if (!value.isEmpty()) {
			return Float.parseFloat(value);
		}
		return null;
	}

	public String mapToString(Object value) {
		return String.valueOf(value);
	}

	public byte mapToByte(String value) {
		int x = 0;
		if (!mapToString(value).isEmpty()) {
			x = Integer.valueOf(value);
		}
		return (byte) x;
	}

	public boolean mapToBoolean(String value) {
		return Boolean.parseBoolean(value);
	}

}
