package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.service.AiTools.ContextTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final UserDataService userDataService;

    public AiService(ChatClient.Builder chatClientBuilder, UserDataService userDataService) {
        this.chatMemory = new InMemoryChatMemory();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(
                        "You are a friendly chatbot with deep knowledge of data architectures. Always answer questions with the understanding that you are assisting a software engineer."
                )
                .build();

        this.userDataService = userDataService;
    }

    public String chat(String message){
        String conversationId = "007";

        return chatClient.prompt()
                .tools(new ContextTools())
                .user(message)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .call()
                .content();
    }

    //TODO DB'den çektiğimiz veriyle session contexti oluştuyoruz
    public String buildSessionContext(String sessionId, String username) {
        //TODO user'ı db'den çekmeye gerek yok sanırsam onu da parametre olarak alırız chatRequestle

        //TODO user id ile user_data tablosundan user'a ait tüm cevapları çekeriz,
        // cevapların sorularını tablodaki question key ile teker teker çekebilriz
        // ama tabii bu işi optimize etmek lazım
        List<UserDataResponse> allDataOfUser = userDataService.getAllDataOfUser(username).getBody();

        String template = """
                    User: {username}
                    GivenAnswers: {answers}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);

        return promptTemplate.createMessage(Map.of(
                "username", allDataOfUser.getUsername(),
                "answers", allDataOfUser
        ));
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