package fr.uit.univparis8.tpair.tpair1.api.mapper;

import fr.uit.univparis8.tpair.tpair1.api.common.dto.CategoryDto;
import fr.uit.univparis8.tpair.tpair1.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
}
