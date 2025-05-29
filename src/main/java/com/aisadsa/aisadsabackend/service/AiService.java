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
    @Autowired
    private RecommendationService recommendationService;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatMemory = new InMemoryChatMemory();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(
                        "You are a friendly chatbot with deep knowledge of data architectures. Always answer questions with the understanding that you are assisting a software engineer, and keep your answers concise and on point."
                )
                .build();
    }

    public String chat(ChatRequest chatRequest) {
        String sessionId = chatRequest.getSessionId();

        return chatClient.prompt()
                .messages(buildSessionContext(sessionId, chatRequest.getUsername()))
                .user(chatRequest.getMessage())
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                .call()
                .content();
    }

    public String generateDocument(ChatRequest chatRequest) {
        String sessionId = chatRequest.getSessionId();

        String systemPrompt = /*"""
                     Generate document content for the advice according to adviceText, questions and user's answers for them.
                     It should be at least two pages long and include comprehensive information covering all aspects of the topic,
                     including background information, current trends or developments, relevant statistics or data, key concepts or
                     theories, potential challenges, and future outlook. The document should be well-structured with clear headings
                     and sub-headings, and it should provide an in-depth analysis that offers insights and engages the reader effectively.
                """*/
               /* """
                        Generate an explanatory text that clearly communicates the rationale behind the system's selected data architecture recommendation. The explanation should be based on the system result, the underlying rules triggered in the business rule engine, and the user’s responses to the session questions.
                
                        The text should include:
                        A concise overview of the user's context and goals as derived from the session answers.
                        A breakdown of the key business rules or decision points that influenced the recommendation.
                        An interpretation of how each relevant factor (e.g., data volume, structure, latency requirements, analytics needs, etc.) led to the final decision.
                        A summary of the recommended architecture and why it fits the user’s needs.
                        Any trade-offs or limitations associated with the recommendation.
                        Future considerations or next steps that the user may explore based on this decision.

                        The text should be clear, professional, and informative, providing enough depth to help the user understand and trust the system’s decision. Use structured headings and paragraphs for readability
                """*/
                """
                        Generate a detailed technical explanation of the system’s recommended data architecture, based on the user's responses and the rules triggered in the underlying business rule engine.

                        The explanation should:
                        Start with a technical summary of the user's input context, including key architectural requirements (e.g., data types, expected load, latency tolerance, processing style, storage preferences).
                        Explain how each response influenced outcomes and contributed to the selection of the final architecture.
                        Provide a technically grounded justification for the selected architecture (e.g., Modern Data Warehouse, Data Lake, Hybrid Lambda Architecture), referencing known patterns, scalability considerations, and data processing models.
                        Outline the strengths, trade-offs, and assumptions associated with the recommendation.
                        Suggest potential enhancements or future extensions (e.g., ML integration, data governance improvements, tool stack optimizations).

                        The output should be structured with appropriate headings and subheadings for clarity, use technical terminology appropriately, and assume the reader has a solid understanding of data systems and architectural design patterns.
                """
                ;

        return chatClient
                .prompt()
                .messages(buildSessionContext(sessionId, chatRequest.getUsername()))
                .system(systemPrompt)
                .user("Create this document using system prompts")
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                .call()
                .content();
    }

    public String generateDiagram(ChatRequest chatRequest) {
        String sessionId = chatRequest.getSessionId();

        // todo getMaxRecommendation ile hangisiyse ona göre uygun diyagram promptu ayarlayalım
        String systemPrompt = """
                Generate an architectural diagram according to adviceText, questions and user's answers.
                It should be in mermaid.js format. Don't write any other thing other than architectural diagram code, just the mermaid.js code should be returned.
                """;

        return chatClient
                .prompt()
                .messages(buildSessionContext(sessionId, chatRequest.getUsername()))
                .system(systemPrompt)
                .user("Create this diagram using system prompts")
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
                
                    Results from the Business Rule Engine:
                    {adviceText}
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

        String adviceText = recommendationService.getAdviceText().orElse("");

        Map<String, Object> variables = Map.of(
                "username", username,
                "questions", String.join("\n", questions),
                "answers", String.join("\n", answers),
                "adviceText", adviceText
        );

        PromptTemplate promptTemplate = new PromptTemplate(template, variables);
        return new SystemMessage(promptTemplate.render());
    }
}