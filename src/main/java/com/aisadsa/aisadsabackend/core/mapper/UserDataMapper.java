package com.aisadsa.aisadsabackend.core.mapper;

import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.entity.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserDataMapper {
    UserDataMapper INSTANCE = Mappers.getMapper(UserDataMapper.class);

    UserDataResponse getUserDataResponseFromUserData(UserData userData);

    List<UserDataResponse> getUserDataResponseListFromUserDataList(List<UserData> userDataList);

    UserData getUserDataFromCreateUserDataDto(CreateUserDataDto createUserDataDto);

}
