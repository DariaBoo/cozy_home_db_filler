//package com.cozyhome.onlineshop.productservice.repository;
//
//import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
//import static org.springframework.data.mongodb.core.aggregation.Aggregation.sample;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.bson.types.ObjectId;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.LookupOperation;
//import org.springframework.data.mongodb.core.aggregation.MatchOperation;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//
//import com.cozyhome.onlineshop.productservice.dto.ProductFilterDto;
//import com.cozyhome.onlineshop.productservice.model.Product;
//import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
//import com.cozyhome.onlineshop.productservice.repository.ProductRepositoryCustom;
//import com.cozyhome.onlineshop.productservice.service.CategoryService;
//
//import lombok.RequiredArgsConstructor;
//
//@Repository
//@RequiredArgsConstructor
//public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
//
//	private final MongoTemplate mongoTemplate;
//	private final CategoryService categoryService;
//
//	@Override
//	public List<Product> getRandomByStatus(ProductStatus status, int count) {
//		Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("status").is(status)), sample(count));
//
//		List<Product> result = mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
//		return result;
//	}
//
//	@Override
//	public List<Product> getRandomByStatusAndCategoryId(ProductStatus status, List<ObjectId> categoriesIds, int count) {
//		Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("categoryId").in(categoriesIds)),
//				match(Criteria.where("status").is(status)), sample(count));
//
//		List<Product> result = mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
//		return result;
//	}
//
//	@Override
//	public List<Product> filterProductsByCriterias(ProductFilterDto filter, Pageable page) {
//		final Query query = new Query().with(page);
//		List<ObjectId> categoriesIds;
//		if (filter.getParentCategoryId() != null) {
//			categoriesIds = filter.getCategoriesIds() != null ? convertToObjectIdList(filter.getCategoriesIds())
//					: categoryService.getCategoriesIdsByParentId(filter.getParentCategoryId());
//
//			query.addCriteria(Criteria.where("categoryId").in(categoriesIds));
//		} else {
//			throw new IllegalArgumentException("ParentCategoryId is required.");
//		}
//
//		List<String> productsSkuCodes;
//		if (filter.getColorsIds() != null) {
//			productsSkuCodes = findByCategoryIdAndColorId(categoriesIds, filter.getColorsIds()).stream()
//					.map(Product::getSkuCode).toList();
//			query.addCriteria(Criteria.where("skuCode").in(productsSkuCodes));
//		}
//
//		if (filter.getMaterialsIds() != null) {
//			List<ObjectId> materialsIds = convertToObjectIdList(filter.getMaterialsIds());
//			query.addCriteria(Criteria.where("materials").in(materialsIds));
//		}
//
//		if (filter.getCollectionsIds() != null) {
//			List<ObjectId> collectionsIds = convertToObjectIdList(filter.getCollectionsIds());
//			query.addCriteria(Criteria.where("collection").in(collectionsIds));
//		}
//
//		if (filter.getPriceMin() != null && filter.getPriceMax() != null) {
//			query.addCriteria(Criteria.where("price").lte(filter.getPriceMax()).gte(filter.getPriceMin()));
//		}
//
//		if (filter.isSale()) {
//			query.addCriteria(Criteria.where("discount").gt(0));
//		}
//
//		if (filter.getWeightMax() > 0 && filter.getWeightMin() > 0) {
//			query.addCriteria(Criteria.where("weight").lte(filter.getWeightMax()).gte(filter.getWeightMin()));
//		}
//
//		if (filter.getHeightMax() > 0 && filter.getHeightMin() > 0) {
//			query.addCriteria(Criteria.where("height").lte(filter.getHeightMax()).gte(filter.getHeightMin()));
//		}
//
//		if (filter.getWidthMax() > 0 && filter.getWidthMin() > 0) {
//			query.addCriteria(Criteria.where("width").lte(filter.getWidthMax()).gte(filter.getWidthMin()));
//		}
//
//		if (filter.getDepthMax() > 0 && filter.getDepthMin() > 0) {
//			query.addCriteria(Criteria.where("depth").lte(filter.getDepthMax()).gte(filter.getDepthMin()));
//		}
//
//		if (filter.getNumberOfDoorsMin() > 0 && filter.getNumberOfDoorsMax() > 0) {
//			query.addCriteria(Criteria.where("numberOfDoors").lte(filter.getNumberOfDoorsMax())
//					.gte(filter.getNumberOfDoorsMin()));
//		}
//
//		if (filter.getNumberOfDrawersMin() > 0 && filter.getNumberOfDrawersMax() > 0) {
//			query.addCriteria(Criteria.where("numberOfDrawers").lte(filter.getNumberOfDrawersMax())
//					.gte(filter.getNumberOfDrawersMin()));
//		}
//
//		if (filter.getBedLengthMin() > 0 && filter.getBedLengthMax() > 0) {
//			query.addCriteria(Criteria.where("bedLength").lte(filter.getBedLengthMax()).gte(filter.getBedLengthMin()));
//		}
//
//		if (filter.getBedWidthMin() > 0 && filter.getBedWidthMax() > 0) {
//			query.addCriteria(Criteria.where("bedWidth").lte(filter.getBedWidthMax()).gte(filter.getBedWidthMin()));
//		}
//
//		if (filter.getLoadMin() > 0 && filter.getLoadMax() > 0) {
//			query.addCriteria(Criteria.where("maxLoad").lte(filter.getLoadMax()).gte(filter.getLoadMin()));
//		}
//
//		return mongoTemplate.find(query, Product.class);
//	}
//	
//	public List<Product> filterProductsByCriterias2(ProductFilterDto filter, Pageable page) {
//	    List<ObjectId> categoriesIds;
//	    if (filter.getParentCategoryId() != null) {
//	        categoriesIds = filter.getCategoriesIds() != null
//	                ? convertToObjectIdList(filter.getCategoriesIds())
//	                : categoryService.getCategoriesIdsByParentId(filter.getParentCategoryId());
//	    } else {
//	        throw new IllegalArgumentException("ParentCategoryId is required.");
//	    }
//	    
//	    List<Product> filteredProducts = new ArrayList<>();
//		if (filter.getColorsIds() != null) {
//			filteredProducts = findByCategoryIdAndColorId(categoriesIds, filter.getColorsIds());
//		}
//
//		filteredProducts = filteredProducts.stream()
//                .filter(product -> filter.getMaterialsIds() == null || product.getMaterials().stream().anyMatch(material -> filter.getMaterialsIds().contains(material.getId().toString())))
//                .filter(product -> filter.getCollectionsIds() == null || filter.getCollectionsIds().contains(product.getCollection().getId().toString()))
//                .filter(product -> filter.getPriceMin() == null || product.getPrice().compareTo(filter.getPriceMin()) >= 0)
//                .filter(product -> filter.getPriceMax() == null || product.getPrice().compareTo(filter.getPriceMax()) <= 0)
//                .filter(product -> !filter.isSale() || product.getDiscount() > 0)
//                .filter(product -> filter.getWeightMax() <= 0 || (product.getWeight() >= filter.getWeightMin() && product.getWeight() <= filter.getWeightMax()))
//                .filter(product -> filter.getHeightMax() <= 0 || (product.getHeight() >= filter.getHeightMin() && product.getHeight() <= filter.getHeightMax()))
//                .filter(product -> filter.getWidthMax() <= 0 || (product.getWidth() >= filter.getWidthMin() && product.getWidth() <= filter.getWidthMax()))
//                .filter(product -> filter.getDepthMax() <= 0 || (product.getDepth() >= filter.getDepthMin() && product.getDepth() <= filter.getDepthMax()))
//                .filter(product -> filter.getNumberOfDoorsMin() <= 0 || (product.getNumberOfDoors() >= filter.getNumberOfDoorsMin() && product.getNumberOfDoors() <= filter.getNumberOfDoorsMax()))
//                .filter(product -> filter.getNumberOfDrawersMin() <= 0 || (product.getNumberOfDrawers() >= filter.getNumberOfDrawersMin() && product.getNumberOfDrawers() <= filter.getNumberOfDrawersMax()))
//                .filter(product -> filter.getBedLengthMin() <= 0 || (product.getBedLength() >= filter.getBedLengthMin() && product.getBedLength() <= filter.getBedLengthMax()))
//                .filter(product -> filter.getBedWidthMin() <= 0 || (product.getBedWidth() >= filter.getBedWidthMin() && product.getBedWidth() <= filter.getBedWidthMax()))
//                .filter(product -> filter.getLoadMin() <= 0 || (product.getMaxLoad() >= filter.getLoadMin() && product.getMaxLoad() <= filter.getLoadMax()))
//                .collect(Collectors.toList());
//	    
//	    int start = (int) page.getOffset();
//	    int end = Math.min((start + page.getPageSize()), filteredProducts.size());
//	    if (start > end) {
//	        return Collections.emptyList();
//	    }
//	    return filteredProducts.subList(start, end);
//	}
//
//	private List<Product> findByCategoryIdAndColorId(List<ObjectId> categoriesIds, List<String> colorsIds) {
//		List<ObjectId> colorsIdsObjectId = convertToObjectIdList(colorsIds);
//		LookupOperation lookup = LookupOperation.newLookup().from("dataImage").localField("skuCode")
//				.foreignField("productSkuCode").as("image");
//
//		MatchOperation matchCategoryId = Aggregation.match(new Criteria("categoryId").in(categoriesIds));
//		MatchOperation matchColorId = Aggregation.match(new Criteria("image.color.$id").in(colorsIdsObjectId));
//
//		Aggregation aggregation = Aggregation.newAggregation(lookup, matchCategoryId, matchColorId);
//
//		return mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
//	}
//
//	private List<ObjectId> convertToObjectIdList(List<String> stringList) {
//		return stringList.stream().map(ObjectId::new).toList();
//	}
//
//}
