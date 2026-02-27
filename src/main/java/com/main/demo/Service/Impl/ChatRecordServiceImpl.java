package com.main.demo.Service.Impl;

import com.main.demo.Mapper.SpringAIChatRecordMapper;
import com.main.demo.Service.ChatRecordService;
import com.main.demo.entity.po.SpringAiChatRecord;
import com.main.demo.entity.vo.MessageVO;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRecordServiceImpl implements ChatRecordService {
    @Autowired
    private SpringAIChatRecordMapper springAIChatRecordMapper;

    @Override
    public void saveRecord(Long user_id, String conversationId ,String type) {
        // 这里可以调用ChatHistoryRepository来保存记录
        Integer counts  = springAIChatRecordMapper.getById(user_id.intValue(), conversationId);
        if(counts!=null && counts > 0){
            return;
        }

        // 创建po类
        SpringAiChatRecord springAiChatRecord = new SpringAiChatRecord();
        springAiChatRecord.setId(conversationId);
        springAiChatRecord.setUserId(user_id.longValue());
        springAiChatRecord.setType(type);
        springAiChatRecord.setTitle(conversationId);
        springAiChatRecord.setCreateTime(LocalDateTime.now());

        // 插入数据库
        springAIChatRecordMapper.insert(springAiChatRecord);
    }

    @Override
    public List<String> findConversationIds(String type) {
        // 这里可以调用ChatHistoryRepository来获取记录
        Integer user_id = 1; // 这里可以从上下文中获取当前用户的id
        List<String> conversationIds = springAIChatRecordMapper.findConversationIds(user_id, type);
        return conversationIds;
    }


    /**
     * 查找对应的历史记录
     * @param type
     * @param chatId
     * @param userId
     * @return
     */
    public List<MessageVO> get(String type, String chatId, Long userId){
       List<MessageVO> messages = springAIChatRecordMapper.getMessages(type, chatId, userId);
       return messages;
    }

}
