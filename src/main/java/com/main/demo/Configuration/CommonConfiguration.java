package com.main.demo.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Autowired
    ChatMemoryRepository chatMemoryRepository; //默认是InMemoryRepository

    @Bean
    public ChatMemory inmemory_chatMemory(ChatMemoryRepository repo) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repo)
                .maxMessages(50) // 最多保留 50 条
                .build();
    }

    @Bean
    public ChatMemory jdbc_chatMemory(JdbcChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(200)
                .build();
    }

    @Bean
    public ChatClient open_ai_chatClient(OpenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个智能客服，用简洁的语气说话")
//                .defaultSystem("你是一个可爱的萌妹子，用可爱的语气说话")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(inmemory_chatMemory(chatMemoryRepository)).build()
                )
                .build();
    }

    @Bean
    public ChatClient ollama_chatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个智能客服，用简洁的语气说话")
//                .defaultSystem("你是一个可爱的萌妹子，用可爱的语气说话")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(inmemory_chatMemory(chatMemoryRepository)).build()
                )
                .build();
    }
}
