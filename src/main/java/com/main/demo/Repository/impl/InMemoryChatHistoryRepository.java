package com.main.demo.Repository.impl;

import com.main.demo.Repository.ChatHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 将所有的记录都存储在内存中
@Service
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    private Map<String, List<String>> chats = new HashMap<>();

    @Override
    public void save(String type, String chatId){
        if (!chats.containsKey(type)) {
            chats.put(type, new ArrayList<>());
        }
        List<String> chatIds = chats.get(type);
        chatIds.add(chatId);
    }

    // 获得所有的会话id
    public List<String> getChatIds(String type){
        return chats.getOrDefault(type, List.of());
    }

}


