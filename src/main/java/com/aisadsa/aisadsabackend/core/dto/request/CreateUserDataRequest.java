package com.aisadsa.aisadsabackend.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDataRequest {

    @NotBlank(message = "UserData is required!")
    String UserData;
}
