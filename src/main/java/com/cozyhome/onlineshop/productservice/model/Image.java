package com.cozyhome.onlineshop.productservice.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Document(collection = "dataImage")
public class Image {

	@Id
	private ObjectId id;
	private String imagePath;
	private String imageSize;

	public Image(String imagePath) {
		this.imagePath = imagePath;
	}

	public enum ImageSize {
		SMALL_PRODUCT("180x120"),
		LARGE_PRODUCT("304x350"),
		SMALL_CATEGORY("304x250"),
		LARGE_CATEGORY("640x250");

		private String size;

		private ImageSize(String size) {
			this.size = size;
		}
		
		public String getSize() {
			return size;
		}
	}
}
