package com.cozyhome.onlineshop.fill_database;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.repository.CategoryRepository;
import com.cozyhome.onlineshop.repository.CollectionRepository;
import com.cozyhome.onlineshop.repository.ColorRepository;
import com.cozyhome.onlineshop.repository.ImageCategoryRepository;
import com.cozyhome.onlineshop.repository.ImageProductRepository;
import com.cozyhome.onlineshop.repository.MaterialRepository;
import com.cozyhome.onlineshop.repository.ProductRepository;

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
