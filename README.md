# Java + AI 示例项目

这是一个基于 **Spring Boot 3 + Java 17** 的 AI 对话后端示例。

## 功能
- 提供 `POST /api/chat` 接口
- 接收用户问题并调用 AI 模型
- 返回模型回答

## 快速启动

### 1) 配置环境变量

```bash
export OPENAI_API_KEY="你的API Key"
# 可选
export OPENAI_BASE_URL="https://api.openai.com/v1"
export OPENAI_MODEL="gpt-4o-mini"
```

### 2) 运行

```bash
mvn spring-boot:run
```

### 3) 调用接口

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"请给我一个Java学习路线"}'
```

## 返回示例

```json
{
  "reply": "你可以从 Java 基础语法开始..."
}
```

## 目录结构

```text
src/main/java/com/example/aiproject
├── AiProjectApplication.java
├── config
│   ├── AiConfig.java
│   └── AiProperties.java
├── controller
│   └── ChatController.java
├── model
│   ├── ChatRequest.java
│   └── ChatResponse.java
└── service
    └── AiChatService.java
```
