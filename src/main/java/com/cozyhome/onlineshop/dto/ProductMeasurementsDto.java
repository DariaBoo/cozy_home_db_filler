package com.cozyhome.onlineshop.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductMeasurementsDto {
	
	private String bedLength;
    private String bedWidth;
    private String weight;
    private String height;
    private String width;
    private String depth;

}
