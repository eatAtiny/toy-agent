package com.agent.toy.pojo;

import lombok.Data;

@Data
public class TodoItem {
    /**
     * 任务ID
     */
    private String id;
    /**
     * 任务内容
     */
    private String text;
    /**
     * 任务状态
     * 可选值：PENDING, IN_PROGRESS, COMPLETED
     */
    private String status;
}