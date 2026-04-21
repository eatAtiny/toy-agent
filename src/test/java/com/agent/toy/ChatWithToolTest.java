package com.agent.toy;

import com.agent.toy.service.ChatService;
import com.agent.toy.tool.TerminalTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ToyAgentApplication.class)
public class ChatWithToolTest {

    @Resource
    public ChatService chatService;

    @Resource
    public TerminalTool terminalTool;

    @Test
    public void testTerminalTool(){
        String result = terminalTool.executeBash("pwd");
        System.out.println(result);
    }

    @Test
    public void chatWithTool(){
        String result = chatService.chat("南京今天的天气怎么样");
        System.out.println(result);
    }

}
