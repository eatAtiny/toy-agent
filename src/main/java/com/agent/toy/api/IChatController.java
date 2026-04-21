package com.agent.toy.api;

import com.agent.toy.api.dto.ApiResponse;
import com.agent.toy.api.dto.ChatRequest;

public interface IChatController {

    ApiResponse<String> ask(ChatRequest chatRequest);

    ApiResponse<String> command(String command);
}
