package com.main.demo.Controller;


import com.main.demo.Repository.impl.InMemoryChatHistoryRepository;
import com.main.demo.Service.ChatRecordService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    @Qualifier("open_ai_chatClient") //存在多个client时，去进行区分。
    ChatClient open_ai_chat_client;

//    @Autowired
//    InMemoryChatHistoryRepository inMemoryChatHistoryRepository;

    @Autowired
    private ChatRecordService chatRecordService;

    // 注意看返回值，是Flux<String>，也就是流式结果，另外需要设定响应类型和编码，不然前端会乱码
    @RequestMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> fluxChat(@RequestParam(defaultValue = "讲个笑话") String prompt, String chatId) {
//        inMemoryChatHistoryRepository.save("chat", chatId);

        Long user_id = 1L;
        chatRecordService.saveRecord(user_id, chatId, "chat"); // 这里的type就由给定的网络路径确定

        return open_ai_chat_client
                .prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream() // 流式调用
                .content();
    }
}
