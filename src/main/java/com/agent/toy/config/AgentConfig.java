package com.agent.toy.config;

import com.agent.toy.pojo.TaskPlan;
import com.agent.toy.pojo.TodoItem;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface AgentConfig {

    // 规划代理接口
    interface PlanAgent {
        @UserMessage("""
        你是专业的任务规划师。
        用户需求：{{userInput}}
        1. 把用户的总需求拆解成【有序、可执行、原子化】的子任务
        2. 子任务如果要调用工具，必须使用已有的工具
        3. 所有任务初始状态必须设为 PENDING
        4. 必须严格按照指定的JSON格式返回，不要额外文字
        5. 任务列表中不能包含重复的任务
        """
        )
        @Agent("制定详细的行动计划")
        TaskPlan createPlan(@V("userInput") String userInput);
    }

    interface ExecutorAgent {
        @UserMessage("""
        你是严格的任务执行器。根据输入的{{taskPlan}}，执行所有任务。
        1. 按照任务ID【顺序执行】所有任务
        2. 每执行完一个任务，立即更新状态：
           - 执行成功 → COMPLETED
           - 执行失败 → FAILED
           注意状态一定要是大写
        3. 必须调用工具完成任务，禁止编造答案，在调用搜索工具的时候不要输入model参数
        4. 最终返回执行总结
        """)
        @Agent("根据任务列表执行任务")
        String executeTasks(@V("taskPlan") TaskPlan taskPlan);
    }

}