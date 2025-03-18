package com.aisadsa.aisadsabackend.core.dto;

import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDataDto {
    User user;
    Question question;
    String userData;
}
