package com.walker.parkingmanagement.web.dto.mapper;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.web.dto.CreateUserDTO;
import com.walker.parkingmanagement.web.dto.ResponseUserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(CreateUserDTO createUserDTO){ //Método que converte de userDTO para user
        return new ModelMapper().map(createUserDTO,User.class);
    }
    public static ResponseUserDTO toDTO(User user){
        String roleResponseDTO = user.getRole().name().substring("ROLE_".length());
        PropertyMap<User,ResponseUserDTO> properties = new PropertyMap<User, ResponseUserDTO>() {
            @Override
            protected void configure() {
                map().setRole(roleResponseDTO);
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(properties);
        return modelMapper.map(user,ResponseUserDTO.class);
    }
    public static List<ResponseUserDTO> toListDTO(List<User> users){
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }
}
