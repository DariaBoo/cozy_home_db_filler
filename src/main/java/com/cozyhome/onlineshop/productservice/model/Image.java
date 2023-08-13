package com.cozyhome.onlineshop.productservice.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Image {

	private String popUpImageName;
    private String desktopImageName;
    private String previewImageName;
    private String mobileImageName;
    private String sliderImageName;
}
