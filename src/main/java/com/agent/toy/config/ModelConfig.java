package com.agent.toy.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ModelConfig {
    
    @Value("${langchain4j.openai.base-url}")
    private String baseUrl;
    @Value("${langchain4j.openai.api-key}")
    private String apiKey;
    @Value("${langchain4j.openai.model-name}")
    private String modelName;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl) // 基础URL
                .apiKey(apiKey) // API密钥
                .modelName(modelName) // 模型名称
                .timeout(Duration.ofSeconds(10000)) // 超时时间（毫秒）
                .temperature(0.7) // 温度参数
                .topP(0.7) // TopP参数
                .presencePenalty(1.0) // 腾讯混元要求 presence_penalty 在 [1.0, 2.0] 范围内
                .frequencyPenalty(0.0) // 频率惩罚参数
                .logRequests(true) // 打印请求日志
                .logResponses(true) // 打印响应日志
                .build();
    }

    @Bean
    public OpenAiStreamingChatModel openAiStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl) // 基础URL
                .apiKey(apiKey) // API密钥
                .modelName(modelName) // 模型名称
                .timeout(Duration.ofSeconds(10)) // 超时时间（毫秒）
                .temperature(0.7) // 温度参数
                .topP(0.7) // TopP参数
                .presencePenalty(1.0) // 腾讯混元要求 presence_penalty 在 [1.0, 2.0] 范围内
                .frequencyPenalty(0.0) // 频率惩罚参数
                .logRequests(true) // 打印请求日志
                .logResponses(true) // 打印响应日志
                .build();
    }


}