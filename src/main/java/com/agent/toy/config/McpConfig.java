package com.agent.toy.config;

import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class McpConfig {

    /**
     * 百度Ai搜索MCP
     * @param mcpUrl mcpUrl
     * @param apiKey apiKey
     * @return mcpClient实例
     */
    @Bean
    public McpClient baiduAiSearchMcpClient(@Value("${baidu.mcp.ai-search.url}") String mcpUrl,
                                             @Value("${baidu.mcp.ai-search.api-key}") String apiKey) {
        // 创建 MCP 传输层
        McpTransport transport =  StreamableHttpMcpTransport.builder()
                .url(mcpUrl)
                .customHeaders(() -> {
                    Map<String, String> headers = Map.of(
                            "Authorization", "Bearer " + apiKey
                    );
                    return headers;
                })
                .logRequests(true)
                .logResponses(true)
                .build();

        // 创建 MCP 客户端
        return new DefaultMcpClient.Builder()
                .key("BaiduAiSearchMcpClient")
                .transport(transport)
                .build();
    }

//    /**
//     * 百度搜索MCP
//     * @param mcpUrl mcpUrl
//     * @param apiKey apiKey
//     * @return mcpClient实例
//     */
//    @Bean
//    public McpClient baiduWebSearchMcpClient(@Value("${baidu.mcp.web-search.url}") String mcpUrl,
//                                            @Value("${baidu.mcp.web-search.api-key}") String apiKey) {
//        // 创建 MCP 传输层
//        McpTransport transport =  StreamableHttpMcpTransport.builder()
//                .url(mcpUrl)
//                .customHeaders(() -> {
//                    Map<String, String> headers = Map.of(
//                            "Authorization", "Bearer " + apiKey
//                    );
//                    return headers;
//                })
//                .logRequests(true)
//                .logResponses(true)
//                .build();
//
//        // 创建 MCP 客户端
//        return new DefaultMcpClient.Builder()
//                .key("BaiduWebSearchMcpClient")
//                .transport(transport)
//                .build();
//    }


}
