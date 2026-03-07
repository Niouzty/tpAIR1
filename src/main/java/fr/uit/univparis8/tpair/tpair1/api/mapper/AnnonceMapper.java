package fr.uit.univparis8.tpair.tpair1.api.mapper;

import fr.uit.univparis8.tpair.tpair1.api.annonce.dto.AnnonceRequest;
import fr.uit.univparis8.tpair.tpair1.api.annonce.dto.AnnonceResponse;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AnnonceMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    AnnonceResponse toResponse(Annonce entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    Annonce toEntity(AnnonceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(AnnonceRequest request, @MappingTarget Annonce entity);
}
