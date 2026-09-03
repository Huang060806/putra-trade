# Java Backend Engineer 学习管理系统

## 1. Current Learning Status

### 基本信息
- **身份**: 准大二计算机科学学生
- **目标**: 成为 Java Backend Engineer，具备实习申请能力
- **当前主线**: 黑马程序员 Web基础 — Spring Boot Web 案例课程
- **学习风格问题**: 看课能懂，缺编码练习和项目实践
- **GitHub**: https://github.com/Huang060806/TestProject
- **MySQL**: 官方安装版 9.7.2（`/usr/local/mysql`，开机自启；root 密码为安装时设置。2026-08-23 已排查并卸载了端口冲突的 Homebrew 版）

### 已接触的基础知识
| 技能 | Level (0-5) | 说明 |
|------|:-----------:|------|
| Java | 2 | 接触过，但掌握不完全扎实 |
| SQL | 2 | 接触过，但掌握不完全扎实 |
| Data Structures | 2 | 接触过，但掌握不完全扎实 |
| Git | 2 | 接触过，但掌握不完全扎实 |
| Spring Boot | 3 | Putra Trade 全栈项目实战（三模块/JWT/MyBatis/事务/定时任务/WebSocket） |
| HTTP / REST API | 3 | 40+ 接口设计联调，JWT 双密钥认证 |
| Controller / Service / Repository | 1 | 随课程推进中 |
| MyBatis / JPA | 3 | XML动态SQL、注解SQL、PageHelper分页 实战 |
| MySQL | 2 | 基础接触 |
| Redis | 2 | 缓存热点列表、平台开关、密码连接 实战 |
| Testing | 0 | 未接触 |
| Docker / Deployment | 1 | Nginx 前后端分离部署（本机生产模式），Docker 待学 |
| Linux | 0 | 未接触 |
| System Design | 0 | 未接触 |
| Frontend Basics | 2 | Vue3 + Vant/Element Plus 两端联调（AI协作） |

### 学习记录

#### 2026-09-03
- **Topic**: Putra Trade 项目全栈落地（后端 Phase 1-5 + 前端两端 + 部署）
- **What I learned**:
  - Maven 三模块工程（common/pojo/server）与依赖管理（dependencyManagement）
  - JWT 双密钥认证 + 拦截器 + ThreadLocal 上下文；BCrypt 替代 MD5
  - MyBatis XML 动态 SQL（if/choose/set）+ PageHelper 分页；按查询场景倒推索引设计
  - 事务边界设计：下单锁商品、想要+计数同事务、唯一索引幂等防刷
  - Redis 缓存热点数据 + 主动失效；缓存 vs 直打的规模权衡（want_count 案例）
  - Spring Task cron 定时取消超时订单；WebSocket 在线推送 + 消息落库兜底
  - DFA 字典树敏感词过滤 + 待审核状态机（内容平台标配架构）
  - Nginx 前后端分离部署：静态托管、反向代理、WebSocket upgrade、SPA try_files
- **踩坑记录**:
  - 自定义 HttpMessageConverter 排首位会把 springdoc 的 byte[] 响应序列化成 Base64，接口文档白屏 → 改用 Jackson2ObjectMapperBuilderCustomizer
  - Nginx 默认 server 与后端争 8080 端口；brew services 托管进程残留导致 bind 失败
  - MyBatis-Plus 与 MyBatis 混用、pom 未引依赖的半成品工程无法编译（已重构）
- **Project**: Putra Trade（putra-trade/ + user-web/ + admin-web/）本机生产部署完成


#### 2026-08-18
- **Topic**: HTTP协议 → SpringBoot Web案例 → 三层架构 → IOC/DI入门
- **Course**: 黑马程序员 Web基础 — Spring Boot Web 案例
- **What I learned**:
  - HTTP协议（请求/响应格式）
  - SpringBoot Web 案例实战
  - 三层架构（Controller / Service / Repository）
  - IOC（控制反转）和 DI（依赖注入）入门概念
- **What I implemented**: *（待补充）*
- **Problems**: *（待补充）*
- **Confidence**: HTTP=3, Spring Boot Web=2, 三层架构=2, IOC/DI=1
- **Next step**: IOC/DI 深入 + 动手写代码验证理解

---

## 2. Skill Map (动态更新)

| 技能 | Level | 最近更新 | 更新依据 |
|------|:-----:|---------|---------|
| Java | 2 | — | 已知基础，待验证扎实程度 |
| Data Structures | 2 | — | 已知基础，待验证扎实程度 |
| SQL | 2 | — | 已知基础，待验证扎实程度 |
| Git/GitHub | 2 | — | 已知基础，待验证扎实程度 |
| HTTP / REST API | 3 | 2026-08-18 | 学完HTTP协议，能解释请求/响应 |
| Spring Boot | 2 | 2026-08-18 | 完成Web案例，开始IOC/DI |
| Controller/Service/Repository | 2 | 2026-08-18 | 学完三层架构 |
| MySQL | 2 | — | 已有基础 |
| MyBatis / JPA | 1 | — | 随课程推进 |
| IOC / DI | 1 | 2026-08-18 | 入门，待实践验证 |
| Redis | 0 | — | 未接触 |
| Testing | 0 | — | 未接触 |
| Docker / Deployment | 0 | — | 未接触 |
| Linux | 0 | — | 未接触 |
| System Design | 0 | — | 未接触 |
| Frontend Basics | 0 | — | 未接触 |

---

## 3. Current Project

**Putra Trade** — UPM 校园二手交易平台（苍穹外卖 → 二手交易改造）— 完整规划见 [UPM_MARKET_ROADMAP.md](./UPM_MARKET_ROADMAP.md)

- **你的主线**：后端（跟课学知识点 → 翻译成二手交易业务）
- **AI 负责**：前端两端搭建（管理端 Vue3 + 用户端 H5）、接口文档、联调与 Debug 支持
- **状态**：Phase 0 初稿完成（数据库设计/接口骨架/原型图在 docs/ 目录），待你审阅后定稿

---

## 4. Next 3 Actions

1. **告诉我你今天学了什么** — 课程章节、代码实现、遇到的问题
2. **开始今天的学习** — 告诉我你正在看的课程进度
3. **今天结束前说"今天结束了"** — 我会输出 Daily Review

---

*系统已就绪。请告诉我你今天的具体学习内容。*
