package com.agent.toy.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 终端操作工具类
 * 提供执行命令行操作的功能
 */
@Component
public class TerminalTool {

    // ======================== 核心工具：bash ========================
    @Tool(
            name = "bash",
            value = "Execute a shell/bash command on the local system."
    )
    public String executeBash(
            @P("The bash command to execute, e.g. ls, pwd, echo hello. Be aware that the terminal commands vary depending on the environment.") String command
    ) {
        // 生产环境必须加：危险命令拦截
        if (isDangerous(command)) {
            return "Error: Dangerous command is not allowed: " + command;
        }

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "powershell",
                    "-Command",
                    command
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();

            // 读取输出（修复中文乱码）
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK") // Windows GBK 编码
            );
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();

        } catch (Exception e) {
            return "Command failed: " + e.getMessage();
        }
    }

    // ======================== 安全工具：危险命令拦截 ========================
    private boolean isDangerous(String command) {
        String lower = command.toLowerCase();
        return lower.contains("rm -rf") || lower.contains("mkfs") || lower.contains("dd");
    }
}