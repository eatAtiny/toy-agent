package com.agent.toy.service;

import com.agent.toy.tool.TerminalTool;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatModel openAiChatModel;
    @Autowired
    private TerminalTool terminalTool;


    // 内部接口定义，用于带有工具的 AI 助手
    private interface AssistantWithTool {
        String chat(String userMessage);
    }

    public String command(String message) {
        return terminalTool.executeBash(message);
    }

    public String chat(String message) {
        AssistantWithTool assistant = AiServices.builder(AssistantWithTool.class)
                .chatModel(openAiChatModel)
                .tools(terminalTool)
                .build();

        return assistant.chat(message);
    }

}
