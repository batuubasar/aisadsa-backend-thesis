package com.aisadsa.aisadsabackend.core.mapper;

import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDTO;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.entity.Question;
import com.aisadsa.aisadsabackend.entity.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDataMapper {
    UserDataMapper INSTANCE = Mappers.getMapper(UserDataMapper.class);

    UserDataResponse getUserDataResponseFromUserData(UserData userData);

    UserData getUserDataFromCreateUserDataDTO(CreateUserDataDTO createUserDataDTO);

}
