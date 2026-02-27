select sm.type as role, sm.content as content from spring_ai_chat_record sr join spring_ai_chat_memory sm on sm.conversation_id = sr.id
where sr.user_id = 123456
and sr.type='chat'
and sr.id='1772369165813'
order by sm.timestamp desc
limit 200;


