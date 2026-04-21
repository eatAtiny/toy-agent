package com.agent.toy.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作工具类
 * 提供文件读写功能
 */
@Component
public class FileTool {

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @return 文件内容
     */
    @Tool(
            name = "readFile",
            value = "Read the content of a file"
    )
    public String readFile(
            @P("The path of the file to read, e.g. ./example.txt") String filePath
    ) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return "Error: File does not exist: " + filePath;
            }
            if (!file.isFile()) {
                return "Error: Path is not a file: " + filePath;
            }

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            return content.toString();

        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    /**
     * 写入文件内容
     * @param filePath 文件路径
     * @param content 文件内容
     * @param append 是否追加内容
     * @return 操作结果
     */
    @Tool(
            name = "writeFile",
            value = "Write content to a file"
    )
    public String writeFile(
            @P("The path of the file to write, e.g. ./example.txt") String filePath,
            @P("The content to write to the file") String content,
            @P("Whether to append content to the file (true) or overwrite it (false)") boolean append
    ) {
        try {
            // 确保父目录存在
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8)))
            {
                writer.write(content);
            }

            return "File written successfully: " + filePath;

        } catch (Exception e) {
            return "Error writing file: " + e.getMessage();
        }
    }

    /**
     * 列出目录中的文件
     * @param directoryPath 目录路径
     * @return 文件列表
     */
    @Tool(
            name = "listFiles",
            value = "List files in a directory"
    )
    public String listFiles(
            @P("The path of the directory to list files from, e.g. ./") String directoryPath
    ) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                return "Error: Directory does not exist: " + directoryPath;
            }
            if (!directory.isDirectory()) {
                return "Error: Path is not a directory: " + directoryPath;
            }

            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                return "Directory is empty: " + directoryPath;
            }

            StringBuilder result = new StringBuilder();
            result.append("Files in directory '").append(directoryPath).append("':\n");
            for (File file : files) {
                if (file.isDirectory()) {
                    result.append("[DIR]  ").append(file.getName()).append("\n");
                } else {
                    result.append("[FILE] ").append(file.getName()).append(" (").append(file.length()).append(" bytes)\n");
                }
            }

            return result.toString();

        } catch (Exception e) {
            return "Error listing files: " + e.getMessage();
        }
    }

    /**
     * 检查文件是否存在
     * @param filePath 文件路径
     * @return 检查结果
     */
    @Tool(
            name = "fileExists",
            value = "Check if a file exists"
    )
    public String fileExists(
            @P("The path of the file to check, e.g. ./example.txt") String filePath
    ) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isFile()) {
                    return "File exists: " + filePath + " (" + file.length() + " bytes)";
                } else {
                    return "Path exists but is not a file: " + filePath;
                }
            } else {
                return "File does not exist: " + filePath;
            }

        } catch (Exception e) {
            return "Error checking file: " + e.getMessage();
        }
    }
}