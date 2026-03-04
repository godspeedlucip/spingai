package com.main.demo.Configuration;

import com.main.demo.Constant.SystemConstants;
import com.main.demo.tool.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Autowired
    private CourseTools courseTools;

    // 显式创建一个基于内存的ChatMemoryRepository实例，并注册为Bean，这样Spring就会使用这个实例，而不是自动配置的JdbcChatMemoryRepository
    @Bean(name="inMemoryChatMemoryRepository")
    public InMemoryChatMemoryRepository inMemoryChatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    // chat memory部分
    @Bean(name="inmemory_chatMemory")
    public ChatMemory inmemory_chatMemory(@Qualifier("inMemoryChatMemoryRepository") InMemoryChatMemoryRepository inMemoryChatMemoryRepository) {
        // 这里的参数会自动匹配到bean
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(inMemoryChatMemoryRepository)
                .maxMessages(200) // 最多保留 50 条
                .build();
    }

    @Bean(name="jdbc_chatMemory")
    public ChatMemory jdbc_chatMemory(@Qualifier("jdbcChatMemoryRepository") JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(200)
                .build();
    }

    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel openAiEmbeddingModel){
        // 传入embedding model，底层会自动调用这个模型来生成向量并存储
        return SimpleVectorStore.builder(openAiEmbeddingModel).build();
    }

    // "chat"下的client
    @Bean(name="open_ai_chatClient")
    public ChatClient open_ai_chatClient(OpenAiChatModel model, @Qualifier("jdbc_chatMemory")ChatMemory memory) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个智能客服，用简洁的语气说话")
//                .defaultSystem("你是一个可爱的萌妹子，用可爱的语气说话")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .build();
    }

    @Bean(name="ollama_chatClient")
    public ChatClient ollama_chatClient(OllamaChatModel model, @Qualifier("jdbc_chatMemory") ChatMemory memory) {
        return ChatClient.builder(model)
                .defaultSystem("你是一个智能客服，用简洁的语气说话")
//                .defaultSystem("你是一个可爱的萌妹子，用可爱的语气说话")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .build();
    }

    // "service"下的client，增加一些工具
    @Bean(name="service_chatClient")
    public ChatClient service_chatClient(OpenAiChatModel model, @Qualifier("inmemory_chatMemory") ChatMemory memory) {
        return ChatClient.builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .defaultTools(courseTools)
                .build();
    }

    // "pdf"下的client，增加向量检索工具
    @Bean(name="pdfChatClient")
    public ChatClient pdfChatClient(
            OpenAiChatModel model,
            @Qualifier("jdbc_chatMemory") ChatMemory memory,
            VectorStore vectorStore) {
        return ChatClient.builder(model)
                .defaultSystem("根据上下文内容来回答用户的问题，如果无法从上下文中找到答案，请说不知道，不要编造答案")
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        MessageChatMemoryAdvisor.builder(memory).build(),
                        QuestionAnswerAdvisor
                                .builder(vectorStore)
                                .searchRequest(
                                        SearchRequest.builder() // 向量检索的请求参数
                                                .similarityThreshold(0.5d) // 相似度阈值
                                                .topK(2) // 返回的文档片段数量
                                                .build()
                                ).build()
                )
                .build();
    }
}
