package com.agent.toy.controller;

import com.agent.toy.api.dto.ApiResponse;
import com.agent.toy.api.dto.ChatRequest;
import com.agent.toy.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
@Slf4j
public class ChatController {

    @Autowired
    public ChatService chatService;

    /**
     * 处理聊天请求
     * @param chatRequest 聊天请求结构体
     * 包含用户消息、对话历史ID和模型名称
     * @return ApiResponse 包含AI助手回复的响应
     */
    @PostMapping("/chat")
    @ResponseBody
    public ApiResponse<String> chat(@RequestBody ChatRequest chatRequest) {
        return ApiResponse.success(chatService.chat(chatRequest.getMessage()));
    }

    /**
     * 处理命令请求
     * @param command 命令消息内容
     * @return ApiResponse 包含命令执行结果的响应
     */
    @PostMapping("/command")
    @ResponseBody
    public ApiResponse<String> command(@RequestBody String command) {
        return ApiResponse.success(chatService.command(command));
    }


}
