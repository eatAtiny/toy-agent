# Toy Agent Terminal - 使用说明

## 🚀 快速开始

### 1. 启动后端

```bash
# 在项目根目录
mvn spring-boot:run
```

后端将运行在 `http://127.0.0.1:8080`

**重要：确保在 Spring Boot 中添加 CORS 支持**

方式一：在 Controller 方法上添加 @CrossOrigin
```java
@PostMapping("/chat")
@ResponseBody
@CrossOrigin(origins = "*")
public ApiResponse<String> chat(@RequestBody ChatRequest chatRequest) { ... }

@PostMapping("/command")
@ResponseBody
@CrossOrigin(origins = "*")
public ApiResponse<String> command(@RequestBody String command) { ... }
```

方式二：创建全局配置类
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
```

### 2. 启动前端

```bash
cd web
node simple-server.js
```

前端将运行在 `http://localhost:3000`

### 3. 访问应用

在浏览器中打开：`http://localhost:3000`

## 📁 项目结构

```
toy-agent/
├── web/                          # 前端文件
│   ├── index.html               # 主页面
│   ├── terminal.css             # 样式文件
│   ├── terminal.js              # 终端逻辑
│   ├── simple-server.js         # 静态文件服务器
│   └── config.js                # 配置文件
├── src/
│   └── main/java/com/agent/toy/ # 后端 Java 代码
└── pom.xml                       # Maven 配置
```

## 🎯 终端命令

### 基础命令
- `help` - 显示帮助信息
- `clear` - 清空终端屏幕

### 模式切换
- `\agent` - 切换到 Agent 模式（AI 对话）
- `\exit` - 退出 Agent 模式，返回终端模式

### 终端模式命令
- `pwd` - 显示当前目录
- `ls` - 列出文件
- 其他系统命令

### Agent 模式
- 直接输入问题 - 与 AI 对话

### 快捷键
- `Ctrl+C` - 中断当前操作
- `Ctrl+L` - 清空屏幕
- `↑/↓` - 浏览命令历史
- `Backspace` - 删除字符（支持中文）

## 🔧 配置说明

### 前端 API 地址配置

在 `terminal.js` 中修改：

```javascript
this.apiBaseUrl = 'http://127.0.0.1:8080';
```

## 🌐 前后端分离架构

### 前端特点
- ✅ 使用 xterm.js 提供终端体验
- ✅ 支持中文输入和删除
- ✅ 双模式切换（终端/Agent）
- ✅ 通过 HTTP API 与后端通信
- ✅ simple-server.js 仅提供静态文件服务，不做代理

### 后端特点
- ✅ 提供 RESTful API
- ✅ `/api/ai/command` - 执行系统命令
- ✅ `/api/ai/chat` - AI 对话
- ✅ 需要配置 CORS 支持

## 📝 API 接口说明

### 1. 命令执行接口

```
POST http://127.0.0.1:8080/api/ai/command
Content-Type: application/json

请求体："pwd"

响应：
{
  "code": 200,
  "message": "success",
  "data": "\nPath\n----\nD:\\code\\toy-agent\n",
  "timestamp": "2026-04-16T23:41:30.4270463"
}
```

### 2. AI 对话接口

```
POST http://127.0.0.1:8080/api/ai/chat
Content-Type: application/json

请求体：
{
  "message": "帮我看看当前文件夹下有哪些文件",
  "conversationId": "",
  "modelName": ""
}

响应：
{
  "code": 200,
  "message": "success",
  "data": "当前目录下有以下文件...",
  "timestamp": "2026-04-16T23:41:30.4270463"
}
```

## 📝 使用示例

### 1. 终端模式

```
user@toy-agent:~$ pwd
D:\code\toy-agent

user@toy-agent:~$ ls
目录: D:\code\toy-agent
Mode                LastWriteTime         Length Name
----                -------------         ------ ----
d-----       2026/4/15      16:25                src
d-----       2026/4/16       0:51                target
d-----       2026/4/15      19:01                web
-a----       2026/4/15      16:19             73 .gitignore
-a----       2026/4/15      16:47           5155 pom.xml
-a----       2026/4/15      16:19             98 README.md
```

### 2. Agent 模式

```
user@toy-agent:~$ \agent
已切换到代理模式 - 现在可以直接输入问题与 AI 对话

agent@toy-agent:~> 你好
AI: 你好！很高兴为你提供帮助。请问你有什么问题或需要了解的信息吗？

agent@toy-agent:~> \exit
已退出代理模式 - 返回终端模式
```

## 🐛 故障排除

### 前端无法连接后端
1. 检查后端服务是否启动
2. 检查 `terminal.js` 中的 `apiBaseUrl` 是否正确
3. 确保后端已配置 CORS 支持
4. 检查浏览器控制台是否有错误信息

### 中文输入问题
- 确保使用最新版本的代码
- 本版本已支持中文输入和删除

## 📚 技术栈

### 前端
- **xterm.js** - 终端模拟器
- **原生 JavaScript** - 无框架依赖
- **CSS3** - 现代化样式
- **Node.js** - 静态文件服务器

### 后端
- **Spring Boot** - Web 框架
- **LangChain4j** - AI 框架
- **Maven** - 构建工具

## 🎨 界面特性

- 🎨 现代化的深色主题设计
- � 彩色的命令提示符
- 📱 响应式设计
- � 双模式实时切换显示
- 📊 实时状态显示（左下角连接状态、右下角模式）
- ✅ 完美支持中文输入和删除

## 📄 许可证

MIT License
