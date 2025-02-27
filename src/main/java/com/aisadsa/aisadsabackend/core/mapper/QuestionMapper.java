package com.aisadsa.aisadsabackend.core.mapper;

import com.aisadsa.aisadsabackend.core.dto.request.CreateQuestionRequest;
import com.aisadsa.aisadsabackend.core.dto.response.QuestionResponse;
import com.aisadsa.aisadsabackend.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionResponse getQuestionResponseFromQuestion(Question question);

    Question getQuestionFromCreateQuestionRequest(CreateQuestionRequest createQuestionRequest);
}
