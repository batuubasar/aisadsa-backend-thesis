package com.aisadsa.aisadsabackend.core.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponse {

    @NotBlank(message = "userData is required!")
    private String userData;
}
