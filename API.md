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
| [发送重置密码验证码](#发送重置密码验证码) | POST | `/api/auth/send-reset-code` | 向指定邮箱发送密码重置验证码 |
| [重置密码](#重置密码) | POST | `/api/auth/reset-password` | 通过邮箱验证码重置密码 |
| [获取访问统计列表](#获取访问统计列表) | POST | `/api/auth/access-stats-list` | 获取所有IP访问统计列表（管理员专用） |
| [获取用户列表](#获取用户列表) | POST | `/api/auth/user-list` | 获取所有用户列表（管理员专用） |
| [删除用户](#删除用户) | POST | `/api/auth/delete-user` | 删除指定用户（管理员专用） |
| [修改用户角色](#修改用户角色) | POST | `/api/auth/update-user-role` | 修改指定用户的角色身份（管理员专用） |
| [获取个人信息](#获取个人信息) | POST | `/api/auth/profile` | 获取当前用户的个人信息 |
| [修改个人信息](#修改个人信息) | POST | `/api/auth/update-profile` | 修改当前用户的个人信息 |
| [获取公开配置](#获取公开配置) | POST | `/api/config/public` | 获取状态为"公开"的配置项 |
| [根据Key获取公开配置](#根据key获取公开配置) | POST | `/api/config/public/by-key` | 根据配置键获取单个公开配置项 |
| [获取访问统计](#获取访问统计) | POST | `/api/config/public/access-stats` | 获取访问者IP、今日访问量和该IP的总访问量 |
| [获取总统计](#获取总统计) | POST | `/api/config/public/total-stats` | 获取全站总访问次数和今日总访问次数 |

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
- 验证码类型必须为"注册"类型，不能使用其他类型验证码

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
- 同一邮箱1分钟内只能发送一次验证码（按类型独立计算）
- 需要配置SMTP邮件服务器才能正常发送
- 验证码类型为"注册"，用于用户注册流程
- 数据库中区分不同类型的验证码，支持注册和密码重置等场景

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

### 发送重置密码验证码
**接口地址**: `POST /api/auth/send-reset-code`

**请求示例**:
```json
{
  "email": "user@example.com"
}
```

**请求字段说明**:
- `email` (必填): 需要重置密码的邮箱地址

**成功返回示例**:
```json
{
  "success": true,
  "message": "密码重置验证码发送成功"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "密码重置验证码发送失败，请稍后重试"
}
```

**状态码**: 200

**注意事项**:
- 验证码为6位随机数字
- 验证码有效期为5分钟
- 同一邮箱1分钟内只能发送一次验证码（按类型独立计算）
- 需要配置SMTP邮件服务器才能正常发送
- 验证码类型为"修改"，用于密码重置流程
- 邮箱必须是已注册用户的邮箱

[返回顶部](#接口汇总)

### 重置密码
**接口地址**: `POST /api/auth/reset-password`

**请求示例**:
```json
{
  "email": "user@example.com",
  "code": "123456",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

**请求字段说明**:
- `email` (必填): 邮箱地址
- `code` (必填): 6位数字验证码
- `newPassword` (必填): 新密码，不少于6位
- `confirmPassword` (必填): 确认密码，必须与newPassword一致

**成功返回示例**:
```json
{
  "success": true,
  "message": "密码重置成功"
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
- 邮箱必须是已注册用户的邮箱
- 验证码有效期为5分钟
- 新密码长度不能少于6位
- 验证码类型必须为"修改"类型，不能使用注册类型验证码
- 密码重置成功后会清除该用户的所有Token，需要重新登录
- 用户状态必须为"正常"才能重置密码

[返回顶部](#接口汇总)

### 获取访问统计列表
**接口地址**: `POST /api/auth/access-stats-list`

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
  "message": "获取访问统计列表成功",
  "totalCount": 15847,
  "totalIpCount": 142,
  "accessStatsList": [
    {
      "id": 1,
      "ip": "192.168.1.100",
      "visitCount": 25,
      "totalCount": 156,
      "createdTime": "2025-01-15T09:30:45",
      "updatedTime": "2025-01-15T16:22:30"
    },
    {
      "id": 2,
      "ip": "127.0.0.1",
      "visitCount": 12,
      "totalCount": 89,
      "createdTime": "2025-01-14T10:15:20",
      "updatedTime": "2025-01-15T15:45:12"
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
  "message": "获取访问统计列表失败"
}
```

**状态码**: 200

**注意事项**:
- 仅限管理员角色访问，普通用户会被拒绝
- 返回所有IP的访问统计信息，按总访问次数降序排列
- totalCount表示全站总访问次数
- totalIpCount表示访问过网站的不同IP总数
- visitCount表示该IP今日的访问次数
- totalCount表示该IP的历史总访问次数
- 管理员Token必须有效且用户状态为"正常"

[返回顶部](#接口汇总)

### 获取用户列表
**接口地址**: `POST /api/auth/user-list`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

**请求字段说明**:
- `token` (必填): 管理员的登录Token

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取用户列表成功",
  "users": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "avatar": "/images/avatar.png",
      "sex": 1,
      "bio": "这是管理员账户",
      "role": "ADMIN",
      "status": "正常",
      "createdTime": "2025-01-10T10:30:45",
      "loginTime": "2025-01-15T16:22:30"
    },
    {
      "id": 2,
      "username": "user001",
      "email": "user001@example.com",
      "avatar": null,
      "sex": 0,
      "bio": null,
      "role": "USER",
      "status": "正常",
      "createdTime": "2025-01-12T14:15:20",
      "loginTime": "2025-01-14T09:45:10"
    }
  ],
  "totalCount": 2
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "权限不足，仅管理员可访问"
}
```

**状态码**: 200

**注意事项**:
- 仅限管理员角色访问，普通用户会被拒绝
- 返回所有用户的基本信息，不包含密码字段
- totalCount表示系统中的用户总数
- 用户信息按创建时间排序
- 管理员Token必须有效且用户状态为"正常"

[返回顶部](#接口汇总)

### 删除用户
**接口地址**: `POST /api/auth/delete-user`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
  "userId": 2
}
```

**请求字段说明**:
- `token` (必填): 管理员的登录Token
- `userId` (必填): 要删除的用户ID

**成功返回示例**:
```json
{
  "success": true,
  "message": "用户删除成功"
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "权限不足，仅管理员可访问"
}
```

```json
{
  "success": false,
  "message": "用户不存在"
}
```

```json
{
  "success": false,
  "message": "不能删除自己的账户"
}
```

```json
{
  "success": false,
  "message": "不能删除唯一的管理员账户"
}
```

**状态码**: 200

**注意事项**:
- 仅限管理员角色访问，普通用户会被拒绝
- 不能删除自己的账户
- 不能删除唯一的管理员账户（至少保留一个管理员）
- 删除用户时会自动删除相关的Token信息
- 管理员Token必须有效且用户状态为"正常"
- 删除操作不可恢复，请谨慎操作

[返回顶部](#接口汇总)

### 修改用户角色
**接口地址**: `POST /api/auth/update-user-role`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
  "userId": 2,
  "role": "ADMIN"
}
```

**请求字段说明**:
- `token` (必填): 管理员的登录Token
- `userId` (必填): 要修改角色的用户ID
- `role` (必填): 新角色，只能是USER或ADMIN

**成功返回示例**:
```json
{
  "success": true,
  "message": "用户角色修改成功",
  "userInfo": {
    "id": 2,
    "username": "user001",
    "email": "user001@example.com",
    "avatar": null,
    "sex": 0,
    "bio": null,
    "role": "ADMIN",
    "status": "正常"
  }
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "权限不足，仅管理员可访问"
}
```

```json
{
  "success": false,
  "message": "用户不存在"
}
```

```json
{
  "success": false,
  "message": "不能修改自己的角色"
}
```

```json
{
  "success": false,
  "message": "不能将唯一的管理员降级为普通用户"
}
```

```json
{
  "success": false,
  "message": "用户角色已经是管理员"
}
```

```json
{
  "success": false,
  "message": "角色值无效，只能是USER或ADMIN"
}
```

**状态码**: 200

**注意事项**:
- 仅限管理员角色访问，普通用户会被拒绝
- 不能修改自己的角色
- 不能将唯一的管理员降级为普通用户（至少保留一个管理员）
- 角色值只能是USER（普通用户）或ADMIN（管理员）
- 管理员Token必须有效且用户状态为“正常”
- 修改成功后返回更新后的用户信息
- 角色修改操作会被记录在系统日志中

[返回顶部](#接口汇总)

### 获取个人信息
**接口地址**: `POST /api/auth/profile`

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
  "message": "获取个人信息成功",
  "userProfile": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "avatar": "/images/avatar.png",
    "sex": 1,
    "bio": "这是我的个人介绍",
    "role": "ADMIN",
    "status": "正常",
    "createdTime": "2025-01-10T10:30:45",
    "loginTime": "2025-01-15T16:22:30"
  }
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "Token无效或已过期"
}
```

**Token为空返回示例**:
```json
{
  "success": false,
  "message": "Token不能为空"
}
```

**状态码**: 200

**注意事项**:
- 需要有效的用户Token，任何已登录用户都可以访问自己的个人信息
- 返回信息不包含密码字段，确保账户安全
- sex字段：0-未知，1-男，2-女
- role字段：USER-普通用户，ADMIN-管理员
- status字段：正常/异常/无效
- 用户状态必须为"正常"才能获取个人信息
- Token过期时会自动删除该Token

[返回顶部](#接口汇总)

### 修改个人信息
**接口地址**: `POST /api/auth/update-profile`

**请求示例**:
```json
{
  "token": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
  "username": "newusername",
  "avatar": "/images/new-avatar.png",
  "sex": 1,
  "bio": "这是我更新后的个人介绍"
}
```

**请求字段说明**:
- `token` (必填): 用户的登录Token
- `username` (选填): 新用户名，长度2-50个字符
- `avatar` (选填): 头像URL，可以为空
- `sex` (选填): 性别，0-未知，1-男，2-女
- `bio` (选填): 个人介绍，最大500个字符

**成功返回示例**:
```json
{
  "success": true,
  "message": "修改个人信息成功",
  "userProfile": {
    "id": 1,
    "username": "newusername",
    "email": "admin@example.com",
    "avatar": "/images/new-avatar.png",
    "sex": 1,
    "bio": "这是我更新后的个人介绍",
    "role": "ADMIN",
    "status": "正常",
    "createdTime": "2025-01-10T10:30:45",
    "loginTime": "2025-01-15T16:22:30"
  }
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "用户名已被使用"
}
```

**Token无效返回示例**:
```json
{
  "success": false,
  "message": "Token无效或已过期"
}
```

**状态码**: 200

**注意事项**:
- 需要有效的用户Token，任何已登录用户都可以修改自己的个人信息
- 所有字段都是可选的，只传入需要修改的字段即可
- 用户名必须唯一，不能与其他用户重复
- 用户名长度必须在2-50个字符之间
- 性别值只能是0、1、2，其他值会报错
- 个人介绍最大长度为500个字符
- 头像和个人介绍可以传入空字符串来清空
- 用户状态必须为"正常"才能修改个人信息
- 修改成功后返回更新后的完整用户信息
- 邮箱、角色、状态等字段不能通过此接口修改

[返回顶部](#接口汇总)

## 配置接口

### 获取公开配置
**接口地址**: `POST /api/config/public`

**请求示例**:
```json
{}
```

**请求字段说明**:
- 该接口无需任何参数，直接发送空的JSON对象即可

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取公开配置成功",
  "configs": [
    {
      "id": 1,
      "configKey": "site_name",
      "description": "网站名称",
      "configValue": "我的博客系统",
      "status": "公开"
    },
    {
      "id": 2,
      "configKey": "site_description",
      "description": "网站描述",
      "configValue": "一个基于Spring Boot的博客系统",
      "status": "公开"
    }
  ]
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "未找到公开配置项"
}
```

**状态码**: 200

**注意事项**:
- 无需Token验证，任何用户都可以访问
- 仅返回状态为"公开"的配置项
- 不会暴露私人配置信息
- 适合前端获取系统基础配置

[返回顶部](#接口汇总)

### 根据Key获取公开配置
**接口地址**: `POST /api/config/public/by-key`

**请求示例**:
```json
{
  "configKey": "site_name"
}
```

**请求字段说明**:
- `configKey` (必填): 配置项的键值

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取配置成功",
  "config": {
    "id": 9,
    "configKey": "site_name",
    "description": "网站名称",
    "configValue": "个人博客系统",
    "status": "公开"
  }
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "配置项不存在或不是公开状态"
}
```

**配置键为空返回示例**:
```json
{
  "success": false,
  "message": "配置键不能为空"
}
```

**状态码**: 200

**注意事项**:
- 无需Token验证，任何用户都可以访问
- 仅返回状态为"公开"的配置项
- 如果配置项不存在或状态为"私人"，将返回失败
- 适合前端获取特定的系统配置

[返回顶部](#接口汇总)

## 统计接口

### 获取访问统计
**接口地址**: `POST /api/config/public/access-stats`

**请求示例**:
```json
{}
```

**请求字段说明**:
- 该接口无需任何参数，直接发送空的JSON对象即可

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取访问统计成功",
  "ip": "192.168.1.100",
  "todayVisits": 25,
  "ipTotalVisits": 156
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "获取访问统计失败"
}
```

**状态码**: 200

**注意事项**:
- 无需Token验证，任何用户都可以访问
- 每次调用接口会自动记录一次访问
- 自动识别客户端真实IP（支持代理服务器）
- todayVisits表示今日全站访问量
- ipTotalVisits表示当前IP的历史总访问量
- 支持IPv4和IPv6地址格式

[返回顶部](#接口汇总)

### 获取总统计
**接口地址**: `POST /api/config/public/total-stats`

**请求示例**:
```json
{}
```

**请求字段说明**:
- 该接口无需任何参数，直接发送空的JSON对象即可

**成功返回示例**:
```json
{
  "success": true,
  "message": "获取总统计成功",
  "totalVisits": 15847,
  "todayTotalVisits": 523
}
```

**失败返回示例**:
```json
{
  "success": false,
  "message": "获取总统计失败"
}
```

**状态码**: 200

**注意事项**:
- 无需Token验证，任何用户都可以访问
- totalVisits表示全站历史总访问次数（所有IP的总次数相加）
- todayTotalVisits表示今日全站总访问次数（今日所有IP访问次数相加）
- 数据基于数据库中的统计表实时计算
- 不会记录访问，仅获取统计数据

[返回顶部](#接口汇总)