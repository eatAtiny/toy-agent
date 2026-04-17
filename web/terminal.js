class TerminalApp {
    constructor() {
        this.term = null;
        this.fitAddon = null;
        this.commandHistory = [];
        this.historyIndex = -1;
        this.currentLine = '';
        this.isAgentMode = false;
        this.apiBaseUrl = 'http://127.0.0.1:8080';
        
        this.init();
    }

    init() {
        this.initTerminal();
        this.setupEventListeners();
        this.updateModeDisplay();
        this.showWelcomeMessage();
    }

    initTerminal() {
        this.term = new Terminal({
            cursorBlink: true,
            fontSize: 14,
            fontFamily: 'Consolas, "Courier New", monospace',
            theme: {
                background: '#1e1e1e',
                foreground: '#e0e0e0',
                cursor: '#4a9eff',
                cursorAccent: '#1e1e1e',
                selection: 'rgba(74, 158, 255, 0.3)',
                black: '#1e1e1e',
                red: '#ff5f56',
                green: '#27c93f',
                yellow: '#ffbd2e',
                blue: '#4a9eff',
                magenta: '#ff79c6',
                cyan: '#8be9fd',
                white: '#e0e0e0',
                brightBlack: '#4a4a4a',
                brightRed: '#ff6e67',
                brightGreen: '#5af78e',
                brightYellow: '#f4f99d',
                brightBlue: '#57c7ff',
                brightMagenta: '#ff6ac1',
                brightCyan: '#9aedfe',
                brightWhite: '#f8f8f2'
            },
            allowProposedApi: true,
            lineHeight: 1.2,
            letterSpacing: 0,
            convertEol: true
        });

        this.fitAddon = new FitAddon.FitAddon();
        this.term.loadAddon(this.fitAddon);
        this.term.loadAddon(new WebLinksAddon.WebLinksAddon());

        const terminalElement = document.getElementById('terminal');
        this.term.open(terminalElement);
        
        this.fitAddon.fit();
        
        requestAnimationFrame(() => {
            this.fitAddon.fit();
        });

        window.addEventListener('resize', () => {
            this.fitAddon.fit();
        });
    }

    setupEventListeners() {
        this.term.onData((data) => {
            switch (data) {
                case '\r':
                    this.handleEnter();
                    break;
                case '\x1b[A':
                    this.handleUpArrow();
                    break;
                case '\x1b[B':
                    this.handleDownArrow();
                    break;
                case '\x7f':
                case '\b':
                    this.handleBackspace();
                    break;
                case '\x03':
                    this.handleCtrlC();
                    break;
                case '\x0c':
                    this.handleCtrlL();
                    break;
                default:
                    if (data.charCodeAt(0) >= 32 || data.length > 1) {
                        this.handleChar(data);
                    }
                    break;
            }
        });
    }

    async handleEnter() {
        this.term.write('\r\n');
        
        const cmd = this.currentLine.trim();
        if (cmd) {
            this.commandHistory.push(this.currentLine);
            this.historyIndex = this.commandHistory.length;
            this.currentLine = '';
            await this.executeCommand(cmd);
        } else {
            this.currentLine = '';
            this.showPrompt();
        }
    }

    handleUpArrow() {
        if (this.historyIndex > 0) {
            this.historyIndex--;
            this.currentLine = this.commandHistory[this.historyIndex];
            this.clearCurrentLine();
            this.term.write(this.currentLine);
        }
    }

    handleDownArrow() {
        if (this.historyIndex < this.commandHistory.length - 1) {
            this.historyIndex++;
            this.currentLine = this.commandHistory[this.historyIndex];
            this.clearCurrentLine();
            this.term.write(this.currentLine);
        } else {
            this.historyIndex = this.commandHistory.length;
            this.currentLine = '';
            this.clearCurrentLine();
        }
    }

    handleBackspace() {
        if (this.currentLine.length > 0) {
            this.currentLine = this.currentLine.slice(0, -1);
            this.clearCurrentLine();
            this.term.write(this.currentLine);
        }
    }

    handleCtrlC() {
        this.term.write('^C\r\n');
        this.currentLine = '';
        this.showPrompt();
    }

    handleCtrlL() {
        this.term.clear();
        this.showPrompt();
    }

    handleChar(char) {
        this.currentLine += char;
        this.term.write(char);
    }

    clearCurrentLine() {
        this.term.write('\r\x1b[K');
        this.term.write(this.getPrompt());
    }

    getPrompt() {
        const yellow = '\x1b[38;2;255;189;46m';
        const blue = '\x1b[38;2;74;158;255m';
        const pink = '\x1b[38;2;255;121;198m';
        const gray = '\x1b[38;2;160;160;160m';
        const reset = '\x1b[0m';
        
        if (this.isAgentMode) {
            const agentPart = `${pink}agent${reset}`;
            const atPart = `${gray}@${reset}`;
            const hostPart = `${pink}toy-agent${reset}`;
            const pathPart = `${yellow}~${reset}`;
            return `${agentPart}${atPart}${hostPart} ${pathPart} > `;
        } else {
            const userPart = `${blue}user${reset}`;
            const atPart = `${gray}@${reset}`;
            const hostPart = `${blue}toy-agent${reset}`;
            const pathPart = `${yellow}~${reset}`;
            return `${userPart}${atPart}${hostPart} ${pathPart} $ `;
        }
    }

    showPrompt() {
        this.term.write(this.getPrompt());
    }

    updateModeDisplay() {
        const modeElement = document.getElementById('currentMode');
        if (this.isAgentMode) {
            modeElement.textContent = '代理';
            modeElement.className = 'mode-name agent-mode';
        } else {
            modeElement.textContent = '终端';
            modeElement.className = 'mode-name terminal-mode';
        }
    }

    showWelcomeMessage() {
        const yellow = '\x1b[38;2;255;189;46m';
        const green = '\x1b[38;2;39;201;63m';
        const cyan = '\x1b[38;2;139;233;253m';
        const reset = '\x1b[0m';
        
        const welcomeText = `
  ${yellow}欢迎使用 Toy Agent 终端${reset}
  
  一个现代化的终端界面，用于与 AI 代理交互
  
  ${green}可用命令:${reset}
    ${cyan}/agent${reset}        - 切换到代理模式
    ${cyan}/exit or /q${reset}   - 退出代理模式，返回终端模式
    ${cyan}help${reset}          - 显示此帮助信息
    ${cyan}clear${reset}         - 清空终端屏幕

`;
        this.term.write(welcomeText);
        this.showPrompt();
    }

    showHelp() {
        const yellow = '\x1b[38;2;255;189;46m';
        const green = '\x1b[38;2;39;201;63m';
        const cyan = '\x1b[38;2;139;233;253m';
        const reset = '\x1b[0m';
        
        const helpText = `
  ${yellow}可用命令${reset}
  
    ${cyan}/agent${reset}        - 切换到代理模式
    ${cyan}/exit or /q${reset}   - 退出代理模式，返回终端模式
    ${cyan}help${reset}          - 显示此帮助信息
    ${cyan}clear${reset}         - 清空终端屏幕
    
  ${green}终端模式:${reset}
    直接输入命令执行，如: pwd, ls, etc.
    
  ${green}代理模式:${reset}
    直接输入问题与 AI 对话

`;
        this.term.write(helpText);
    }

    async executeCommand(command) {
        const trimCmd = command.trim();
        
        if (trimCmd === '/agent') {
            this.switchToAgentMode();
            this.showPrompt();
            return;
        }
        
        if (trimCmd === '/exit' || trimCmd === '/q') {
            this.switchToTerminalMode();
            this.showPrompt();
            return;
        }
        
        switch (trimCmd.toLowerCase()) {
            case 'help':
                this.showHelp();
                this.showPrompt();
                break;
            case 'clear':
                this.term.clear();
                this.showPrompt();
                break;
            default:
                if (this.isAgentMode) {
                    await this.executeChatCommand(trimCmd);
                } else {
                    await this.executeTerminalCommand(trimCmd);
                }
                break;
        }
    }

    switchToAgentMode() {
        if (this.isAgentMode) {
            this.term.write('已经在代理模式中\r\n');
            return;
        }
        this.isAgentMode = true;
        this.updateModeDisplay();
        const pink = '\x1b[38;2;255;121;198m';
        const reset = '\x1b[0m';
        this.term.write(`${pink}已切换到代理模式 - 现在可以直接输入问题与 AI 对话${reset}\r\n`);
    }

    switchToTerminalMode() {
        if (!this.isAgentMode) {
            this.term.write('已经在终端模式中\r\n');
            return;
        }
        this.isAgentMode = false;
        this.updateModeDisplay();
        const cyan = '\x1b[38;2;139;233;253m';
        const reset = '\x1b[0m';
        this.term.write(`${cyan}已返回终端模式${reset}\r\n`);
    }

    async executeTerminalCommand(command) {
        const cyan = '\x1b[38;2;139;233;253m';
        const red = '\x1b[38;2;255;95;86m';
        const reset = '\x1b[0m';
        
        try {
            const response = await fetch(`${this.apiBaseUrl}/api/ai/command`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(command)
            });

            const result = await response.json();
            
            if (result.code === 200 && result.data) {
                this.term.write(result.data);
            } else {
                this.term.write(`${red}错误: ${result.message || '执行失败'}${reset}\r\n`);
            }
        } catch (error) {
            this.term.write(`${red}连接失败: ${error.message}${reset}\r\n`);
            this.term.write(`${cyan}请确保后端服务器运行在 http://127.0.0.1:8080${reset}\r\n`);
        }
        this.showPrompt();
    }

    async executeChatCommand(message) {
        const pink = '\x1b[38;2;255;121;198m';
        const red = '\x1b[38;2;255;95;86m';
        const cyan = '\x1b[38;2;139;233;253m';
        const reset = '\x1b[0m';
        
        this.term.write(`${cyan}正在思考...${reset}\r\n`);
        
        try {
            const response = await fetch(`${this.apiBaseUrl}/api/ai/chat`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    message: message,
                    conversationId: '',
                    modelName: ''
                })
            });

            const result = await response.json();
            
            if (result.code === 200 && result.data) {
                this.term.write(`\r\n${pink}AI:${reset} ${result.data}\r\n`);
            } else {
                this.term.write(`${red}错误: ${result.message || '请求失败'}${reset}\r\n`);
            }
        } catch (error) {
            this.term.write(`${red}连接失败: ${error.message}${reset}\r\n`);
            this.term.write(`${cyan}请确保后端服务器运行在 http://127.0.0.1:8080${reset}\r\n`);
        }
        this.showPrompt();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new TerminalApp();
});