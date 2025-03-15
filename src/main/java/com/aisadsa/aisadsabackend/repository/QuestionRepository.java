package com.aisadsa.aisadsabackend.repository;

import com.aisadsa.aisadsabackend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    Optional<Question> findByQuestionKey(String questionKey);
    Question findByQuestionId(UUID questionId);
}
