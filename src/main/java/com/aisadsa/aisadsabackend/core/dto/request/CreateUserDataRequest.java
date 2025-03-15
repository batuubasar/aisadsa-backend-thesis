package com.aisadsa.aisadsabackend.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDataRequest {

    UUID questionId;
    @NotBlank(message = "UserData is required!")
    String UserData;
}
