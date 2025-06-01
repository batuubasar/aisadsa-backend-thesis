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
                        Provide a technically grounded justification for the selected architecture (e.g., Modern Data Warehouse, Data Lake, Data Warehouse, Data Lakehouse, Data Fabric, Data Mesh), referencing known patterns, scalability considerations, and data processing models.
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
        String username = chatRequest.getUsername();
        String recommendation_architecture = recommendationService.getLatestRecommendationArchitectureByUsername(username);

        String systemPrompt = """
                        Generate an architectural diagram according to adviceText, questions and user's answers. It should be in mermaid.js format.
                        Don't write any other thing other than architectural diagram code, just the mermaid.js code should be returned.
                        Keep the architecture below as long as it fits all the user related data and if there is anything has to be changed,
                        just change those minor things.
                """;

        switch (recommendation_architecture) {
            case "Data Warehouse" -> systemPrompt += """
                    If the user answered 'Yes' to the streaming (real-time) question, then include both 'B -- Real-Time Processing --> C' and
                    'B -- Batch Processing --> C' in the diagram. However, since this is a Data Warehouse architecture where real-time is generally
                    not typical, only add the real-time flow if the user's answer explicitly requires it. Otherwise, do not include it. Do not change
                     any other parts of the diagram unnecessarily.
                    Diagram:
                            flowchart TD
                             subgraph subGraph0["Data Warehouse Architecture"]
                                    A["Data Sources"]
                                    B["Data Ingestion"]
                                    C["Data Processing"]
                                    M["Model - RDW"]
                              end
                             subgraph subGraph1["User Interaction"]
                                    F["Analytics & Reporting"]
                                    G["User Access"]
                              end
                                A -- Structured Data --> B
                                B -- Batch Processing --> C
                                C --> M
                                M --> F
                                F --> G
                    
                                M@{ shape: db}
                    """;
            case "Data Lake" -> systemPrompt += """
                    In the flow 'A -- Streaming & Structured & Unstructured Data --> B', analyze nonRelationalUsage,
                                streaming(For example, if the answer to the streaming question is Yes, then include Streaming Data) and other related answers to decide whether to include or remove Streaming, Structured or Unstructured.
                    In the lines 'B -- Real-Time Processing --> C' and 'B -- Batch Processing --> C', choose real-time or batch based
                     on the user's answer about processing type. Also, include or exclude components like
                     'Machine Learning' based on relevant answers. Stay aligned with
                     the architectural diagram I’ll provide below — treat it as the main reference structure.Only update elements that
                     already exist in the diagram based on the user's answers: if it's already appropriate, keep it; if it's missing and
                     required, add it; if it’s irrelevant, remove it.
                     Diagram:
                         flowchart TD
                         subgraph subGraph0["Data Lake Architecture"]
                                A["Data Sources"]
                                B["Data Ingestion"]
                                C["Data Processing"]
                                D["Data Storage - Data Lake"]
                                I["Machine Learning"]
                          end
                         subgraph subGraph1["User Interaction"]
                                F["Analytics & Reporting"]
                                G["User Access"]
                          end
                            A -- Streaming & Structured & Unstructured Data --> B
                            B -- "Real-Time Processing" --> C
                            B -- Batch Processing --> C
                            C --> D
                            D --> I & F
                            F --> G
                    
                            D@{ shape: db}
                    """;
            case "Modern Data Warehouse" -> systemPrompt += """
                    In the flow 'A -- Streaming & Structured & Unstructured Data --> B', analyze nonRelationalUsage,
                                streaming(For example, if the answer to the streaming question is Yes, then include Streaming Data) and other related answers to decide whether to include or remove Streaming, Structured or Unstructured.
                    If the user answered 'Yes' to the streaming (real-time) question, then include both 'B -- Real-Time Processing --> C'
                    and 'B -- Batch Processing --> C' in the diagram. Add the 'Real-Time' flow between B and C accordingly
                        Diagram:
                            flowchart TD
                             subgraph subGraph0["Modern Data Warehouse Architecture"]
                                    A["Data Sources"]
                                    B["Data Ingestion"]
                                    C["Data Processing"]
                                    D["Data Storage - Data Lake"]
                                    I["Machine Learning"]
                                    M["Model - RDW"]
                              end
                             subgraph subGraph1["User Interaction"]
                                    F["Analytics & Reporting"]
                                    G["User Access"]
                              end
                                A -- Streaming & Structured & Unstructured Data --> B
                                B -- Batch Processing --> C
                                C --> D
                                D --> M & I
                                M --> F
                                F --> G
                    
                                D@{ shape: db}
                                M@{ shape: db}
                    """;
            case "Data Fabric" -> systemPrompt += """
                    In the flow 'A -- Streaming & Structured & Unstructured Data --> B', analyze nonRelationalUsage,
                                streaming(For example, if the answer to the streaming question is Yes, then include Streaming Data) and other related answers to decide whether to include or remove Streaming, Structured or Unstructured.
                    In the lines 'B -- Real-Time Processing --> C' and 'B -- Batch Processing --> C', choose real-time or batch based
                     on the user's answer about processing type. Also, include or exclude components like 'Master Data Management',
                     'Machine Learning', 'Advanced Security' or 'Relational/BI Access' based on relevant answers. Stay aligned with
                     the architectural diagram I’ll provide below — treat it as the main reference structure.Only update elements that
                     already exist in the diagram based on the user's answers: if it's already appropriate, keep it; if it's missing and
                     required, add it; if it’s irrelevant, remove it.
                            Diagram:
                                flowchart TD
                                 subgraph subGraph0["Data Fabric Architecture"]
                                        A["Data Sources"]
                                        B["Data Ingestion"]
                                        C["Data Processing"]
                                        MDM["Master Data Management"]
                                        D["Data Storage - Data Lake"]
                                        M["Model - RDW"]
                                        I["Machine Learning"]
                                        SL["Serving Layer / BI Access"]
                                        SEC["Advanced Security"]
                                  end
                                 subgraph subGraph1["User Interaction"]
                                        F["Analytics & Reporting"]
                                        G["User Access"]
                                  end
                                    A -- Streaming & Structured & Unstructured Data --> B
                                    B -- "Real-Time Processing" --> C
                                    B -- Batch Processing --> C
                                    C --> MDM
                                    MDM --> D
                                    D --> SEC & I
                                    SEC --> M
                                    I --> SL
                                    M --> SL
                                    SL --> F
                                    F --> G
                                    API["API Layer"] -.-> M & F & G
                                    META["Metadata Layer"] --- C & D & F & MDM & M
                                    D@{ shape: db}
                                    M@{ shape: db}
                                     META:::meta
                                    classDef meta fill:#f8f0d7,stroke:#aaa,stroke-width:1px,font-style:italic
                    """;
            case "Data Lakehouse" -> systemPrompt += """
                    In the flow 'A -- Streaming & Structured & Unstructured Data --> B', analyze nonRelationalUsage,
                                streaming(For example, if the answer to the streaming question is Yes, then include Streaming Data) and other related answers to decide whether to include or remove Streaming, Structured or Unstructured.
                     In the lines 'B -- Real-Time Processing --> C' and 'B -- Batch Processing --> C', choose real-time or batch based
                     on the user's answer about processing type. Also, include or exclude components like 'Master Data Management',
                     'Machine Learning', 'Advanced Security' or 'Relational/BI Access' based on relevant answers. Stay aligned with
                     the architectural diagram I’ll provide below — treat it as the main reference structure.Only update elements that
                     already exist in the diagram based on the user's answers: if it's already appropriate, keep it; if it's missing and
                     required, add it; if it’s irrelevant, remove it.
                            Diagram:
                                flowchart TD
                                 subgraph subGraph0["Data Lakehouse Architecture"]
                                        A["Data Sources"]
                                        B["Data Ingestion"]
                                        C["Data Processing - Delta Engine"]
                                        MDM["Master Data Management"]
                                        D["Unified Data Storage  DataLake + DeltaLake"]
                                        E["Time Travel / ACID Compliance"]
                                        I["Machine Learning"]
                                        SL["Serving Layer - Relational/BI Access"]
                                        SEC["Advanced Security"]
                                  end
                                 subgraph subGraph1["User Interaction"]
                                        F["SQL Analytics & Reporting"]
                                        G["User Access"]
                                  end
                                    A -- Streaming & Structured & Unstructured Data --> B
                                    B -- "Real-Time Processing" --> C
                                    B -- Batch Processing --> C
                                    C --> MDM
                                    MDM --> D
                                    D --> SEC & I
                                    SEC --> E
                                    I --> SL
                                    SL --> F
                                    F --> G
                                    API["API Layer"] -.-> I & F & G
                                    META["Metadata Layer"] --- C & D & F & MDM
                    
                                    D@{ shape: db}
                                     META:::meta
                                    classDef meta fill:#f8f0d7,stroke:#aaa,stroke-width:1px,font-style:italic
                    """;
            case "Data Mesh" -> systemPrompt += """
                            flowchart TD
                             subgraph Domain1["Domain: A"]
                                    D1["Data Processing"]
                                    P1["Data Product"]
                              end
                             subgraph Domain2["Domain: B"]
                                    D2["Data Processing"]
                                    P2["Data Product"]
                              end
                             subgraph subGraph0["Data Mesh Architecture"]
                                    A["Data Sources"]
                                    B["Domain-Oriented Ingestion"]
                                    Domain1
                                    Domain2
                                    I["Cross-Domain Machine Learning"]
                                    M["Data Product Consumption"]
                              end
                             subgraph subGraph1["User Interaction"]
                                    F["Analytics & Reporting"]
                                    G["User Access"]
                              end
                                A --> B
                                B --> D1 & D2
                                D1 --> P1
                                P1 --> M & I
                                D2 --> P2
                                P2 --> M & I
                                I --> M
                                M --> F
                                F --> G
                                PLATFORM["Self-Serve Infrastructure"] --> D1 & D2 & I
                                GOVERN["Federated Governance"] --> P1 & P2
                    
                                 PLATFORM:::platform
                                 GOVERN:::govern
                                classDef platform fill:#f0f8ff,stroke:#3399cc
                                classDef govern fill:#fff0f5,stroke:#cc3366
                    """;
        }
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