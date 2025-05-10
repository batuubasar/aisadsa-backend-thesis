package com.aisadsa.aisadsabackend.service.AiTools;

import org.springframework.ai.tool.annotation.Tool;

public class ContextTools {

    @Tool(description = "Get the current state of the user")
    String getCurrentState() {
        return null;
    }
}
