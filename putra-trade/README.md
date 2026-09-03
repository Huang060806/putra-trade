# Putra Trade · UPM 校园二手交易平台

面向 UPM 学生的校园二手交易平台：卖家发布闲置，买家按分类/地点/关键字浏览搜索，点"我想要"解锁卖家联系方式，支持下单、模拟支付、面交确认全流程；管理员通过后台审核商品、处理举报、封禁用户、查看数据看板。

## 系统架构

```
┌──────────────┐   ┌──────────────┐
│  user-web    │   │  admin-web   │   Vue 3 + Vite（Vant H5 / Element Plus）
│  :8000 (H5)  │   │  :8081 (后台) │
└──────┬───────┘   └──────┬───────┘
       └─────── Nginx ────┘   静态托管 + 反向代理 /user /admin /upload /ws
                │
       ┌────────▼─────────┐
       │ putra-trade      │   Spring Boot 2.7 · Java 17
       │ ├── common       │   Result / ThreadLocal / JWT / BCrypt / DFA
       │ ├── pojo         │   Entity / DTO / VO
       │ └── server       │   Controller / Service / Mapper
       └───┬────┬────┬───┘
        MySQL  Redis  WebSocket
        12表   缓存/开关  实时消息推送
```

## 技术栈

- **后端**：Spring Boot 2.7 + MyBatis + PageHelper + MySQL 8 + Redis + WebSocket + Spring Task + POI + Knife4j，Maven 三模块（common / pojo / server），包名 `com.putra.trade.*`
- **认证**：JWT 双密钥体系（用户端/管理端互不通用）+ 拦截器 + ThreadLocal；密码 BCrypt（弃用课程 MD5 方案）
- **前端**：Vue 3 · user-web = Vant 移动端 H5（12 页）· admin-web = Element Plus + ECharts（7 页）

## 核心设计（面试可讲）

- **商品 5 态状态机**：在售 → 交易中 → 已售出 / 已下架 / 待审核（敏感词命中）
- **内容安全两层防护**：DFA 字典树敏感词扫描（命中进待审核队列，不硬拒）+ 用户举报人工审核闭环
- **"想要"轻重意向分离**：收藏 = 轻意向；想要 = 重意向（解锁卖家联系方式 + 通知卖家 + want_count 热度）
- **want_count 一致性**：唯一索引防重复 + 单行原子 UPDATE + 同事务写入；刻意不上 Redis 计数（文档⑲说明权衡）
- **订单闭环**：下单锁定商品（事务）→ 模拟支付 → 面交确认 → 商品已售；Spring Task 每分钟扫 30 分钟未支付订单自动取消释放
- **热点缓存**：热度排序首页 Redis 缓存 5 分钟，写操作主动失效
- **多态举报表**：target_type + target_id 一张表装商品/用户两种举报

## 本地启动

```bash
# 1. 建库（MySQL 8）
mysql -u root -p < docs/sql/putra_trade.sql

# 2. 后端（JDK 17，依赖 Redis 密码 123456）
cd putra-trade && mvn clean package -DskipTests
java -jar putra-trade-server/target/putra-trade-server-1.0.0.jar
# 接口文档: http://localhost:8080/doc.html

# 3. 前端（开发模式）
cd user-web && npm i && npm run dev    # :8082
cd admin-web && npm i && npm run dev   # :8081

# 4. 生产部署：前端 npm run build，dist 由 Nginx 托管
#    参考 /opt/homebrew/etc/nginx/servers/putra-trade.conf
```

## 测试账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 学生 | BC210001 ~ BC210004 | 123456 |
| 管理员 | admin | admin123 |

## 目录

```
putra-trade/       后端三模块工程
user-web/          用户端 H5（Vue3 + Vant）
admin-web/         管理后台（Vue3 + Element Plus）
docs/              数据库设计、接口文档、原型图、SQL 脚本
```

## 路线图（二期）

- [x] 站内一对一聊天（WebSocket 双向 + 离线轮询兜底，2026-09-03 完成）
- [ ] 商品全文搜索（MySQL LIKE → Elasticsearch）
- [ ] 买卖双方互评 + 信用分
- [ ] uni-app 移植微信小程序
- [ ] Docker Compose 一键部署 + 云服务器上线
