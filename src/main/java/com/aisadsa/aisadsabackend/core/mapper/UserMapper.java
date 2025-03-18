package com.aisadsa.aisadsabackend.core.mapper;

import com.aisadsa.aisadsabackend.core.dto.request.RegisterRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserResponse;
import com.aisadsa.aisadsabackend.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

    UserResponse getUserResponseFromUser(User user);
}