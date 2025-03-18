package com.aisadsa.aisadsabackend.repository;


import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UUID> {

    List<UserData> findByUserId(UUID userId);
    Optional<UserData> findByUserIdAndQuestionId(UUID userId, UUID questionId);

}

