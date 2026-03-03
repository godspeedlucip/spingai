package com.main.demo.Controller;


import com.main.demo.Service.ChatRecordService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ServiceController {

    @Autowired
    @Qualifier("service_chatClient") //存在多个client时，去进行区分。
    ChatClient service_chatClient;

    @Autowired
    private ChatRecordService chatRecordService;

    // 注意看返回值，是Flux<String>，也就是流式结果，另外需要设定响应类型和编码，不然前端会乱码
    @RequestMapping(value = "/service", produces = "text/html;charset=UTF-8")
    public Flux<String> fluxChat(@RequestParam(defaultValue = "讲个笑话") String prompt, String chatId) {

        Long user_id = 1L;
        chatRecordService.saveRecord(user_id, chatId, "service"); // 这里的type就由给定的网络路径确定

        return service_chatClient
                .prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream() // 流式调用
                .content();
    }
}
