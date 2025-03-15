package com.aisadsa.aisadsabackend.entity;

import com.aisadsa.aisadsabackend.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "user_data")
@NoArgsConstructor
@Data
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_data_id", nullable = false)
    private UUID userDataId;


    @Column(name = "user_data", nullable = false)
    private String userData;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}