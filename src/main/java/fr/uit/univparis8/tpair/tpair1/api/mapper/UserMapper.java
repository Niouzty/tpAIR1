package fr.uit.univparis8.tpair.tpair1.api.mapper;

import fr.uit.univparis8.tpair.tpair1.api.common.dto.UserDto;
import fr.uit.univparis8.tpair.tpair1.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
