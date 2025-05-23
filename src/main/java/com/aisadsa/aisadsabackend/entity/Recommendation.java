package com.aisadsa.aisadsabackend.entity;

import com.aisadsa.aisadsabackend.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.Map.entry;

@Entity
@Table(name = "recommendations")
@NoArgsConstructor
@Data
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id", nullable = false)
    private int recommendation_id;

    @Column(name = "recommendation")
    private String recommendation;

    @Column(name = "recommendation_message", columnDefinition = "TEXT")
    private String recommendation_message;

    @ElementCollection
    @CollectionTable(name = "recommendation_scores", joinColumns = @JoinColumn(name = "recommendation_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "score")
    private Map<String, Integer> recommendationScores = new HashMap<>(Map.of(
            "Data Warehouse", 0,
            "Modern Data Warehouse", 0,
            "Data Lake", 0,
            "Data Lakehouse", 0,
            "Data Fabric", 0,
            "Data Mesh", 0
    ));

    @ElementCollection
    @CollectionTable(name = "recommendation_messages", joinColumns = @JoinColumn(name = "recommendation_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "message", columnDefinition = "TEXT")
    private Map<String, String> recommendationMessages = new HashMap<>(Map.ofEntries(
            entry("streamingType", ""),
            entry("updateFrequency", ""),
            entry("mlUsage", ""),
            entry("mdmNeed", ""),
            entry("criticalConfidentiality", ""),
            entry("selfServiceBI", ""),
            entry("dashboardUsage", ""),
            entry("slaRequirement", ""),
            entry("availabilityRequirement", ""),
            entry("customerAccess", ""),
            entry("dataMovementChallenge", ""),
            entry("cloudUsage", "")
    ));
    // (Map.of kullanıyorduk 10dan fazla key veremiyorduk ondan değiştik. versiona baglı hata verebeilir yenisi


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private Stack<String> questionStack = new Stack<>();
    @Transient
    private Set<String> askedQuestions = new HashSet<>();

    public void addQuestionToStack(String questionKey) {
        questionStack.push(questionKey);
    }

    public void addQuestionToAskedQuestions(String questionKey) {
        askedQuestions.add(questionKey);
    }

    public void removeQuestion(String questionKey) {
        if (!questionStack.isEmpty() && questionStack.peek().equals(questionKey)) {
            questionStack.pop();
        }
    }

    public void removeSpecificQuestion(String questionKey) {
        Stack<String> tempStack = new Stack<>();
        while (!questionStack.isEmpty()) {
            String current = questionStack.pop();
            if (current.equals(questionKey)) {
                break;
            }
            tempStack.push(current);
        }
        while (!tempStack.isEmpty()) {
            questionStack.push(tempStack.pop());
        }
    }
    public String getNextQuestionKey() {
        if (!questionStack.isEmpty()) {
            return questionStack.peek();
        }
        else return null;
    }

    public int getRemainingQuestionCount() { return questionStack.size(); }

    public Boolean isQuestionStackEmpty (){
        return questionStack.isEmpty();
    }

    public String getMaxRecommendation() {
        String bestRecommendation = "";
        int maxValue = -1;

        for (Map.Entry<String, Integer> entry : recommendationScores.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                bestRecommendation = entry.getKey();
            }
        }
        return bestRecommendation;
    }

    public String getAllMessage() {
        StringBuilder combinedMessage = new StringBuilder();
        for (String message : recommendationMessages.values()) {
            if (message != null && !message.isBlank()) {
                combinedMessage.append(message).append(" ");
            }
        }
        return combinedMessage.toString().trim();
    }
}