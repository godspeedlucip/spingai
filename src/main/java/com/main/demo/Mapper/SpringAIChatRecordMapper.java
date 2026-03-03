package com.main.demo.Mapper;

import com.main.demo.entity.po.SpringAiChatRecord;
import com.main.demo.entity.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface SpringAIChatRecordMapper {

    /**
     * 根据io来查询
     * @param user_id,  conversation_id
     * @return
     */
    @Select("select  count(0)  from spring_ai_chat_record where user_id=#{user_id} and id=#{conversation_id}")
    Integer getById(Integer user_id, String conversation_id);


    /**
     * 往表里插入一条数据
     * @param springAiChatRecord
     */
    void insert(SpringAiChatRecord springAiChatRecord);

    /**
     * 找到所有的type对应下的conversation_id
     * @param user_id
     * @param type
     * @return
     */
    @Select("select * from spring_ai_chat_record where user_id = #{user_id} and type = #{type}")
    List<String> findConversationIds(Integer user_id, String type);

    /**
     * 查找得到所有的历史记录
     */
    List<MessageVO> getMessages(String type, String chatId, Long userId);
}
