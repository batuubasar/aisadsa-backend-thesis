package com.aisadsa.aisadsabackend.repository;

import com.aisadsa.aisadsabackend.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    @Query("SELECT r FROM Recommendation r WHERE r.user.username = :username ORDER BY r.recommendation_id DESC LIMIT 1")
    Optional<Recommendation> findLatestRecommendationByUsername(@Param("username") String username);
}
