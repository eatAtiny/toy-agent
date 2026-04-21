package com.agent.toy.tool.todo;

import com.agent.toy.pojo.TodoItem;

import java.util.*;

/**
 * 计划管理器
 */
public class TodoManager {
    private List<TodoItem> items = new ArrayList<>();

    /**
     * 更新任务列表
     * @param items 任务列表
     * @return 更新后的任务列表
     */
    public String update(List<TodoItem> items) {
        // 最多20条
        if (items.size() > 20) {
            throw new IllegalArgumentException("Max 20 todos allowed");
        }

        List<TodoItem> validated = new ArrayList<>();
        int inProgressCount = 0;

        for (int i = 0; i < items.size(); i++) {
            TodoItem item = items.get(i);

            String text = item.getText() == null ? "" : item.getText().trim();
            String status = item.getStatus() == null ? "pending" : item.getStatus().toLowerCase();
            String id = item.getId() == null ? String.valueOf(i + 1) : item.getId();

            if (text.isEmpty()) {
                throw new IllegalArgumentException("Item " + id + ": text required");
            }

            if (!List.of("pending", "in_progress", "completed").contains(status)) {
                throw new IllegalArgumentException("Item " + id + ": invalid status: " + status);
            }

            if (status.equals("in_progress")) {
                inProgressCount++;
            }

            TodoItem valid = new TodoItem();
            valid.setId(id);
            valid.setText(text);
            valid.setStatus(status);
            validated.add(valid);
        }

        if (inProgressCount > 1) {
            throw new IllegalArgumentException("Only one task can be in_progress at a time");
        }

        this.items = validated;
        return render();
    }

    /**
     * 渲染任务列表
     * @return 任务列表字符串
     * 格式：
     * [ ] #1: 任务1
     * [>] #2: 任务2
     * [x] #3: 任务3
     * (1/3 completed)
     */
    public String render() {
        if (items.isEmpty()) return "No todos.";

        List<String> lines = new ArrayList<>();
        for (TodoItem item : items) {
            String marker = switch (item.getStatus()) {
                case "pending" -> "[ ]";
                case "in_progress" -> "[>]";
                case "completed" -> "[x]";
                default -> "[?]";
            };
            lines.add(marker + " #" + item.getId() + ": " + item.getText());
        }

        long done = items.stream().filter(t -> t.getStatus().equals("completed")).count();
        lines.add("\n(" + done + "/" + items.size() + " completed)");
        return String.join("\n", lines);
    }

    public List<TodoItem> getItems() {
        return items;
    }
}