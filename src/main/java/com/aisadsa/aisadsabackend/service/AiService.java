package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.request.ChatRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private QuestionService questionService;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatMemory = new InMemoryChatMemory();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(
                        "You are a friendly chatbot with deep knowledge of data architectures. Always answer questions with the understanding that you are assisting a software engineer."
                )
                .build();
    }

    public String chat(ChatRequest chatRequest) {
        String sessionId = "007";

        return chatClient.prompt()
                .messages(buildSessionContext(sessionId, chatRequest.getUsername()))
                .user(chatRequest.getMessage())
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                .call()
                .content();
    }


    public SystemMessage buildSessionContext(String sessionId, String username) {
        String template = """
                    Session Context of the User
                
                    User: {username}
                
                    Questions:
                    {questions}
                
                    Answers:
                    {answers}
                """;

        List<UserDataResponse> allDataOfUser = Optional.ofNullable(userDataService.getAllDataOfUser(username).getBody())
                .orElse(Collections.emptyList());

        List<String> questions = allDataOfUser.stream()
                .map(UserDataResponse::getQuestionKey)
                .map(qKey -> questionService.getQuestionByKey(qKey).getDescription())
                .toList();

        List<String> answers = allDataOfUser.stream()
                .map(UserDataResponse::getUserData)
                .toList();

        Map<String, Object> variables = Map.of(
                "username", username,
                "questions", String.join("\n", questions),
                "answers", String.join("\n", answers)
        );

        PromptTemplate promptTemplate = new PromptTemplate(template, variables);
        return new SystemMessage(promptTemplate.render());
    }




    /*public String generateDocument(String topic) {
        *//* Initialize variable *//*
        PromptTemplate promptTemplate =null;
        try {

            *//* Create a prompt template with placeholders for the topic and document content instructions *//*
            promptTemplate = new PromptTemplate("""
                 Generate document content for a {topic}. 
                 It should be at least two pages long and include comprehensive information covering all aspects of the topic, 
                 including background information, current trends or developments, relevant statistics or data, key concepts or 
                 theories, potential challenges, and future outlook. The document should be well-structured with clear headings
                 and sub-headings, and it should provide an in-depth analysis that offers insights and engages the reader effectively.
            """);

            *//* Replace the placeholder {topic} with the actual topic provided *//*
            promptTemplate.add("topic", topic);
        }catch(Exception e) {
            log.error("error : "+e.getMessage());
        }

        *//* Generate document content using the AI client and return the text of the generated content *//*
        return chatClient.call(promptTemplate.create()).getResult().getOutput().getContent();
    }*/
}