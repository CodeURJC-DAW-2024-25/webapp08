package com.webapp08.pujahoy.dto;

import com.webapp08.pujahoy.model.Product;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductBasicMapper {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "iniValue", target = "iniValue"),
        @Mapping(source = "name", target = "name"),
        @Mapping(target = "imgURL", expression = "java(generateImageUrl(product.getId()))"),
        @Mapping(source = "seller", target = "seller")
    })
    ProductBasicDTO toDTO(Product product);
    List<ProductBasicDTO> toDTOs(Page<Product> products);

    default String generateImageUrl(Long id) {
        return "/products/" + id + "/image";  
    }

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "iniValue", target = "iniValue"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "imgURL", target = "imgURL"),
        @Mapping(source = "seller", target = "seller")
    })
    Product toDomain(ProductBasicDTO productBasicDTO);
}
