package com.agent.toy.pojo;

import lombok.Data;

import java.util.List;

@Data
public class TaskPlan {

    public String goal;
    public List<TodoItem> items;

}
