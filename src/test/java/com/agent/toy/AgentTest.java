package com.agent.toy;

import com.agent.toy.service.AgentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ToyAgentApplication.class)
public class AgentTest {

    @Autowired
    private AgentService agentService;

    @Test
    public void test() {
        String userInput = "用搜索工具帮我看看小林coding网站上的八股，给我列一份学习计划到我存在本地文件中";
        String output = agentService.processTask(userInput);
        System.out.println(output);
    }
}
