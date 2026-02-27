CREATE TABLE `spring_ai_chat_record` (
                                         `id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '会话id' COLLATE 'utf8mb4_general_ci',
                                         `title` VARCHAR(150) NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
                                         `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户id',
                                         `type` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT 'chat:聊天机器人；service：智能客服；pdf：个人知识库' COLLATE 'utf8mb4_general_ci',
                                         `create_time` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP) COMMENT '会话创建时间',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         INDEX `create_time` (`create_time`) USING BTREE
)
