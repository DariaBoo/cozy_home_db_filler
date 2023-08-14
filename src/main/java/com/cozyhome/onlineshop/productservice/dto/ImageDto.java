package com.cozyhome.onlineshop.productservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ImageDto {

	private String popUpImageName;
    private String desktopImageName;
    private String previewImageName;
    private String mobileImageName;
    private String sliderImageName;
}
