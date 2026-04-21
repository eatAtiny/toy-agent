package com.agent.toy.service;

import com.agent.toy.config.AgentConfig;
import com.agent.toy.pojo.TaskPlan;
import com.agent.toy.tool.FileTool;
import com.agent.toy.tool.TerminalTool;
import com.agent.toy.tool.todo.TodoListTool;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AgentService {

    @Autowired
    private OpenAiChatModel openAiChatModel;
    @Autowired
    private McpClient baiduWebSearchMcpClient;
    @Autowired
    private TerminalTool terminalTool;
    @Autowired
    private FileTool fileTool;
    @Autowired
    private TodoListTool todoListTool;

    public String processTask(String userInput) {

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        UntypedAgent executionLoop = AgenticServices
                .loopBuilder()
                .subAgents(
                        AgenticServices.agentBuilder(AgentConfig.ExecutorAgent.class)
                                .chatModel(openAiChatModel)
                                .tools(terminalTool, fileTool, todoListTool)
                                .toolProvider(McpToolProvider.builder().mcpClients(baiduWebSearchMcpClient).build())
                                .outputKey("execution_report")
                                .chatMemory(chatMemory)
                                .build()
                )
                .maxIterations(5) // 设置最大迭代次数
                .exitCondition(agenticScope -> {

                    String execution_report = (String) agenticScope.readState("execution_report");
                    System.out.println("当前执行报告: " + execution_report); // 输出关键结果
                    TaskPlan taskPlan = (TaskPlan) agenticScope.readState("taskPlan");
                    System.out.println("当前计划状态: " + taskPlan); // 输出关键结果
                    // 根据报告内容判断是否退出循环
                    if(taskPlan.getItems().getLast().getStatus().equals("COMPLETED"))
                        return true;
                    else
                        return false;
                })
                .outputKey("execution_report")
                .build();

        UntypedAgent plan_executor = AgenticServices
                .sequenceBuilder()
                .subAgents(
                        AgenticServices.agentBuilder(AgentConfig.PlanAgent.class)
                                .chatModel(openAiChatModel)
                                .tools(terminalTool, fileTool, todoListTool)
                                .toolProvider(McpToolProvider.builder().mcpClients(baiduWebSearchMcpClient).build())
                                .outputKey("taskPlan")
                                .build(),
                        executionLoop
                ) // 每次循环先生成计划，再执行计划
                .outputKey("execution_report")
                .build();

        Map<String, Object> agentInput = new HashMap<>();
        agentInput.put("userInput", userInput);

        String finalReport = (String) plan_executor.invoke(agentInput);
        return finalReport;
    }
}