package com.main.demo.Controller;

import com.main.demo.Repository.impl.InMemoryChatHistoryRepository;
import com.main.demo.Service.ChatRecordService;
import com.main.demo.entity.vo.MessageVO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai/history")
public class HistoryController {
//    @Autowired
//    private InMemoryChatHistoryRepository inMemoryChatHistoryRepository;

    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    @Qualifier("jdbc_chatMemory")
    ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatList(@PathVariable String type){
//        return inMemoryChatHistoryRepository.getChatIds(type);
        return chatRecordService.findConversationIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId){
        // 这里只能获取内存中的记录
        Long user_id = 1L;
        List<MessageVO> messages  = chatRecordService.get(type, chatId, user_id);
        if(messages == null) {
            return List.of();
        }
        return messages;
    }

    // 从内存去读取
//    @GetMapping("/{type}/{chatId}")
//    public List<MessageVO> getHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId){
//        // 这里只能获取内存中的记录
//        List<Message> messages = chatMemory.get(chatId);
//
//        if(messages == null) {
//            return List.of();
//        }
//
//        List<MessageVO> vos = new ArrayList<>();
//        for (Message message : messages) {
//            vos.add(new MessageVO(message));
//        }
//        return vos;
//    }
}
