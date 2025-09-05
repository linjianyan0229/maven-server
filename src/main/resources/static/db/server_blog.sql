-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL链接',
  `password` varchar(100) NOT NULL COMMENT 'BC加密密码',
  `sex` tinyint DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `bio` text COMMENT '个人介绍',
  `role` varchar(20) DEFAULT 'USER' COMMENT '角色身份：USER-普通用户，ADMIN-管理员',
  `status` varchar(10) DEFAULT '正常' COMMENT '状态：正常/异常/无效',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- Token表
CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Token ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `token` varchar(255) NOT NULL COMMENT 'Token值',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expire_time` (`expire_time`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户Token表';

-- 验证码表
CREATE TABLE `verification_code` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '验证码ID',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `code` varchar(6) NOT NULL COMMENT '六位数字验证码',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  KEY `idx_email` (`email`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮箱验证码表';

-- 配置项表
CREATE TABLE `config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键值',
  `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `config_value` text NOT NULL COMMENT '配置内容',
  `status` varchar(10) DEFAULT '公开' COMMENT '状态：公开/私人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入配置项数据
INSERT INTO `config` (`config_key`, `description`, `config_value`, `status`) VALUES
('email_account', '邮箱账号', 'your-email@example.com', '私人'),
('email_password', '邮箱账号授权码', 'your-email-password', '私人'),
('email_host', '邮件服务器地址', 'smtp.example.com', '私人'),
('email_port', '邮件服务器端口', '587', '私人'),
('register_email_subject', '发送注册验证码标题', '【博客系统】邮箱验证码', '私人'),
('register_email_content', '注册验证码内容', '您的邮箱验证码是：{code}\n\n验证码有效期为5分钟，请及时使用。\n如果这不是您的操作，请忽略此邮件。\n\n此邮件由系统自动发送，请勿回复。', '私人');
