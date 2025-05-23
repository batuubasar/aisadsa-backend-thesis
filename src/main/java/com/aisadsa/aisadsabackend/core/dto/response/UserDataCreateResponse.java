package com.aisadsa.aisadsabackend.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataCreateResponse {
    private String nextQuestionKey;
    private int remainingQuestionCount;
}
