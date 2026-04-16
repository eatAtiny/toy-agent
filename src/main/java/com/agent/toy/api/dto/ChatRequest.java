package com.agent.toy.api.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 聊天请求结构体
 * 用于接收聊天消息
 */
@Data
public class ChatRequest {

    /**
     * 聊天消息内容
     */
    @NotNull(value = "消息内容不能为空")
    private String message;

    /**
     * 对话历史ID（可选）
     * 用于保持对话上下文
     */
    private String conversationId;

    /**
     * 模型名称（可选）
     * 用于指定使用的AI模型
     */
    private String modelName;
}