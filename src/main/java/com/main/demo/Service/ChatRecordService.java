package com.main.demo.Service;

//import com.baomidou.mybatisplus.extension.service.IService;
import com.main.demo.entity.po.SpringAiChatRecord;
import com.main.demo.entity.vo.MessageVO;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public interface ChatRecordService extends IService<SpringAiChatRecord> {
//    void saveRecord(Integer user_id, String conversationId ,String type);
//
//    List<String> findConversationIds(String type);
//}

public interface ChatRecordService {
    void saveRecord(Long user_id, String conversationId ,String type);

    /**
     * 获取当前type的所有会话id
     * @param type
     * @return
     */
    List<String> findConversationIds(String type);


    /**
     * 查找对应的历史记录
     * @param type
     * @param chatId
     * @param userId
     * @return
     */
    List<MessageVO> get(String type, String chatId, Long userId);
}
