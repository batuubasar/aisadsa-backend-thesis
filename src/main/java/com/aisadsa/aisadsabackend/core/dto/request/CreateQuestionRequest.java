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
public class CreateQuestionRequest {
    @NotBlank(message = "Question Key is required!")
    private String questionKey;
    @NotBlank(message = "Description is required!")
    private String description;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
