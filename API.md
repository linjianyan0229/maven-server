# API接口文档

## 接口汇总

| 接口名称 | 请求方式 | 接口地址 | 功能描述 |
|---------|---------|----------|----------|
| [用户登录](#用户登录) | POST | `/api/auth/login` | 用户通过用户名或邮箱登录系统 |
| [用户注册](#用户注册) | POST | `/api/auth/register` | 用户通过邮箱验证码注册账户 |
| [发送验证码](#发送验证码) | POST | `/api/auth/send-code` | 向指定邮箱发送验证码 |
| [用户登出](#用户登出) | POST | `/api/auth/logout` | 用户退出登录并删除Token |
| [Token验证](#token验证) | POST | `/api/auth/validate-token` | 验证Token有效性并获取用户信息 |
| [获取系统配置](#获取系统配置) | POST | `/api/auth/configs` | 获取所有系统配置（管理员专用） |

---

## 认证接口

### 用户登录
**接口地址**: `POST /api/auth/login`

**请求示例**:
```json
{
  "login": "admin",
  "password": "password", 
  "rememberMe": true
}
```

**请求字段说明**:
- `login` (必填): 用户名或邮箱
- `password` (必填): 用户密码
- `rememberMe` (选填): 是否记住我，true为7天有效期，false或不传为1天有效期

**成功返回示例**:
```json
{
  "success": true,
  "message": "登录成功",
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
  "expireTime": "2025-09-12T14:22:30",
  "userInfo": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "avatar": null,
    "sex": 1,
    "bio": "系统管理员",
    "role": "ADMIN",
    "status": "正常"
  }
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "用户名或密码错误"
}
```

**状态码**: 200

**注意事项**:
- Token需要在后续请求的Header中携带: `Authorization: Bearer {token}`
- 记住我功能：rememberMe=true时Token有效期7天，false时有效期1天
- 登录成功后会清除该用户的所有旧Token
- 用户状态必须为"正常"才能登录

[返回顶部](#接口汇总)

### 用户注册
**接口地址**: `POST /api/auth/register`

**请求示例**:
```json
{
  "email": "user@example.com",
  "code": "123456",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**请求字段说明**:
- `email` (必填): 邮箱地址
- `code` (必填): 6位数字验证码
- `password` (必填): 密码，不少于6位
- `confirmPassword` (必填): 确认密码，必须与password一致

**成功返回示例**:
```json
{
  "success": true,
  "message": "注册成功"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "验证码错误或已过期"
}
```

**状态码**: 200

**注意事项**:
- 邮箱必须符合格式要求
- 验证码有效期为5分钟
- 用户名自动从邮箱前缀生成，如有重复会自动添加数字后缀
- 注册成功后用户状态为"正常"，角色为"USER"

[返回顶部](#接口汇总)

### 发送验证码
**接口地址**: `POST /api/auth/send-code`

**请求示例**:
```json
{
  "email": "user@example.com"
}
```

**请求字段说明**:
- `email` (必填): 接收验证码的邮箱地址

**成功返回示例**:
```json
{
  "success": true,
  "message": "验证码发送成功"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "验证码发送失败，请稍后重试"
}
```

**状态码**: 200

**注意事项**:
- 验证码为6位随机数字
- 验证码有效期为5分钟
- 同一邮箱1分钟内只能发送一次验证码
- 需要配置SMTP邮件服务器才能正常发送

[返回顶部](#接口汇总)

### 用户登出
**接口地址**: `POST /api/auth/logout`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

**请求字段说明**:
- `token` (必填): 用户的登录Token

**成功返回示例**:
```json
{
  "success": true,
  "message": "退出登录成功"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "Token无效或已过期"
}
```

**状态码**: 200

**注意事项**:
- Token验证失败时也会返回失败消息
- 退出登录后Token将被删除，无法再次使用
- 建议前端退出时清除本地存储的Token

[返回顶部](#接口汇总)

### Token验证
**接口地址**: `POST /api/auth/validate-token`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

**请求字段说明**:
- `token` (必填): 需要验证的Token

**成功返回示例**:
```json
{
  "success": true,
  "message": "Token验证成功",
  "valid": true,
  "userInfo": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "avatar": null,
    "sex": 1,
    "bio": "系统管理员",
    "role": "ADMIN",
    "status": "正常"
  }
}
```

**Token无效返回示例**:
```json
{
  "success": true,
  "message": "Token无效或已过期",
  "valid": false
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "Token不能为空",
  "valid": false
}
```

**状态码**: 200

**注意事项**:
- 用于页面刷新时验证Token是否仍然有效
- 验证成功时返回最新的用户信息
- 用户状态异常时Token验证会失败
- 适合前端应用启动时恢复登录状态

[返回顶部](#接口汇总)

### 获取系统配置
**接口地址**: `POST /api/auth/configs`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

**请求字段说明**:
- `token` (必填): 管理员用户的Token

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取配置成功",
  "configs": [
    {
      "id": 1,
      "configKey": "email_account",
      "description": "邮箱账号",
      "configValue": "your-email@example.com",
      "status": "私人"
    },
    {
      "id": 2,
      "configKey": "email_password",
      "description": "邮箱账号授权码",
      "configValue": "your-email-password",
      "status": "私人"
    }
  ]
}
```

**权限不足返回示例**:
```json
{
  "success": false,
  "message": "权限不足，仅管理员可访问"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "未找到配置项"
}
```

**状态码**: 200

**注意事项**:
- 仅限管理员角色访问，普通用户会被拒绝
- 返回所有配置项，包括公开和私人配置
- 包含敏感配置信息，请注意安全性
- 管理员Token必须有效且用户状态为"正常"

[返回顶部](#接口汇总)