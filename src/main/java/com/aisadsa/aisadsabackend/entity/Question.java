package com.aisadsa.aisadsabackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "question_id")
    private UUID id;

    @Column(name = "question_key", unique = true, nullable = false)
    private String questionKey;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "option1")
    private String option1;

    @Column(name = "option2")
    private String option2;

    @Column(name = "option3")
    private String option3;

    @Column(name = "option4")
    private String option4;

    @OneToMany(mappedBy = "question")
    private List<UserData> userResponses = new ArrayList<>();

}
