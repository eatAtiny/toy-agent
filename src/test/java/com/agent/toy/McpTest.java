package com.agent.toy;


import com.agent.toy.service.McpAssistant;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class McpTest {

    @Autowired
    private StreamingChatModel streamingChatModel;

    @Test
    public void testMcp() {
        // 1. 构建MCP协议
        McpTransport transport = new StdioMcpTransport.Builder()
                                .command(List.of("npx.cmd", "-y", "@baidumap/mcp-server-baidu-map"))
                                .environment(Map.of("BAIDU_MAP_API_KEY", "37XsJcV9EsVcajJ67E0p7i3P2wwtC5E4"))
                                .build();
        // 2. 创建MCP Client
        McpClient mcpClient = new DefaultMcpClient.Builder()
                            .transport(transport)
                            .build();
        // 3. 创建工具集
        McpToolProvider toolProvider = McpToolProvider.builder()
                                    .mcpClients(mcpClient)
                                    .build();
        // 4. 实例化Aiservice将大模型工具集告诉我们的Aiservice
        McpAssistant build = AiServices.builder(McpAssistant.class)
                .streamingChatModel(streamingChatModel)
                .toolProvider(toolProvider)
                .build();
        // 5. 进行对话
        StringBuffer sb = new StringBuffer();
        build.chat("帮我查一下南京的这两天的天气").doOnNext(sb::append).blockLast();
        System.out.println(sb.toString());

    }

}
