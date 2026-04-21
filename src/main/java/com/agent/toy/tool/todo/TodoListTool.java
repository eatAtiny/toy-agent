package com.agent.toy.tool.todo;

import com.agent.toy.pojo.TaskPlan;
import com.agent.toy.pojo.TodoItem;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agentic.scope.AgenticScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoListTool {
    private final TodoManager todoManager = new TodoManager();

    @Tool("Update the task list. Mark tasks as pending, in_progress, completed.")
    public String updateTodos(TaskPlan taskPlan, AgenticScope agenticScope) {

        System.out.println("🛠️ 工具执行：更新任务列表状态");

        // 1. 打印一下确认收到的是新状态
        System.out.println("收到的TaskPlan：" + taskPlan);

        // 2. 🔥 关键：手动把更新后的 taskPlan 写回 AgenticScope！
        // 这里的 key "taskPlan" 要和你在 workflow 里用的 key 完全一致
        agenticScope.writeState("taskPlan", taskPlan);

        System.out.println("✅ 已将最新TaskPlan写入AgenticScope");
        return "✅ 已将最新TaskPlan写入AgenticScope";

//        try {
//            return todoManager.update(taskPlan.getItems());
//        } catch (Exception e) {
//            return "Error updating todo: " + e.getMessage();
//        }
    }

    public TodoManager getManager() {
        return todoManager;
    }
}