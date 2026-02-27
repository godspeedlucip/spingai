package com.main.demo.Repository;

import java.util.List;

public interface ChatHistoryRepository {
    // 可以存储多种type的历史消息，例如
    void save(String type, String chatId);

    // 获得所有的会话id
    List<String> getChatIds(String type);

    // 获得特定会话id的会话详情

}
