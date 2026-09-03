# 接口文档骨架 · Putra Trade（UPM 校园二手交易平台）

> 这是 Phase 0 的**接口清单**：定路径和职责，具体参数/响应结构随开发阶段逐步补全。
> 正式使用时把本文档导入 Apifox（新建项目 → 按下面模块建目录），或直接用 Swagger 注解生成。

## 全局约定

### 统一响应格式 (对应课程 Result 类)
```json
{ "code": 1, "msg": "success", "data": {} }
```
`code=1` 成功，`code=0` 失败。分页响应 data 内为 `{ total, records }`。

### 认证方式
- 登录后所有接口携带请求头 `token: <JWT>`（管理端与用户端各自签发）
- 未登录访问受保护接口 → 前端跳转登录页

### 路径前缀
| 端 | 前缀 | 说明 |
|----|------|------|
| 管理端 | `/admin/**` | 管理员 JWT |
| 用户端 | `/user/**` | 学生 JWT |

---

## 一、公共接口

| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST | `/common/upload` | 文件上传（图片），返回 URL | P2 |
| GET | `/common/download` | 文件下载（课程对照） | P2 |

## 二、管理端 `/admin`

### 2.1 员工/管理员模块 employee
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST | `/admin/employee/login` | 管理员登录，返回 JWT | P1 |
| POST | `/admin/employee/logout` | 退出登录 | P1 |
| POST | `/admin/employee` | 新增管理员 | P1 |
| GET  | `/admin/employee/page?page&pageSize&name` | 分页查询 | P1 |
| PUT  | `/admin/employee` | 编辑管理员 | P1 |
| POST | `/admin/employee/status/{status}?id=` | 启用/禁用 | P1 |

> 与课程 Day2-3 完全同构，业务名换成"平台运营人员"。

### 2.2 分类模块 category
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/admin/category/all` | 全部分类(下拉框用) | P2 |
| POST | `/admin/category` | 新增分类 | P2 |
| DELETE | `/admin/category?id=` | 删除分类 | P2 |
| PUT  | `/admin/category` | 编辑分类 | P2 |

### 2.3 商品管理 item
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/admin/item/page?title&categoryId&status&page&pageSize` | 分页查询(多维筛选) | P2 |
| GET  | `/admin/item/{id}` | 商品详情(含图册) | P2 |
| PUT  | `/admin/item/status/{status}?id=&reason=` | 上架/下架；违规下架必填 reason，自动发站内消息通知卖家 | P2 |
| DELETE | `/admin/item/{id}` | 删除商品 | P2 |

### 2.4 用户管理 member
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/admin/member/page?nickname&studentNo&status&page&pageSize` | 分页查询学生 | P2 |
| POST | `/admin/member/status/{status}?id=` | 封禁/解封 | P2 |

### 2.5 订单管理 orders
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/admin/order/page?orderNo&status&page&pageSize` | 分页查询订单 | P4 |
| GET  | `/admin/order/{id}` | 订单详情 | P4 |

### 2.6 数据统计 statistics
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/admin/statistics/overview?begin&end` | 总览卡片: 成交额/成交量/新增用户/新增商品 | P5 |
| GET  | `/admin/statistics/trend?begin&end` | 每日成交趋势(ECharts折线) | P5 |
| GET  | `/admin/statistics/topCategory?begin&end` | 品类成交占比(ECharts饼图) | P5 |
| GET  | `/admin/statistics/export?begin&end` | 导出Excel报表(POI) | P5 |

## 三、用户端 `/user`

### 3.1 认证 auth
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST | `/user/auth/code?email` | 发送验证码到UPM邮箱(Redis存5分钟,限流) | P3 |
| POST | `/user/auth/register` | 注册: 学号+昵称+密码+邮箱验证码 | P3 |
| POST | `/user/auth/login` | 登录: 学号+密码, 返回JWT | P3 |

### 3.2 平台状态 platform（对应课程的店铺营业状态）
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/user/platform/status` | 平台是否开放(Redis) | P3 |

### 3.3 商品浏览与发布 item
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/user/item/hot` | 首页最新在售(Spring Cache缓存) | P3 |
| GET  | `/user/item/page?keyword&categoryId&conditionLevel&minPrice&maxPrice&page&pageSize` | 搜索/筛选 | P3 |
| GET  | `/user/item/{id}` | 详情(view_count+1) | P3 |
| POST | `/user/item` | 发布商品 | P3 |
| PUT  | `/user/item` | 编辑商品(仅卖家本人) | P3 |
| POST | `/user/item/status/{status}?id=` | 卖家自己上架/下架 | P3 |
| GET  | `/user/item/mine` | 我发布的 | P3 |

> 注：hot / page / mine 的列表项 VO 均含 `sellerNickname`（联表 member 查询），首页卡片直接展示。

### 3.4 收藏 favorite
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST   | `/user/favorite/{itemId}` | 收藏 | P3 |
| DELETE | `/user/favorite/{itemId}` | 取消收藏 | P3 |
| GET    | `/user/favorite/list` | 我的收藏列表 | P3 |
| GET    | `/user/favorite/{itemId}` | 是否已收藏(详情页按钮态) | P3 |

### 3.5 面交地点 meet_spot（对应课程地址簿）
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST   | `/user/spot` | 新增面交点 | P4 |
| DELETE | `/user/spot?id=` | 删除 | P4 |
| PUT    | `/user/spot` | 编辑 | P4 |
| GET    | `/user/spot/list` | 我的面交点列表 | P4 |

### 3.6 订单 orders
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| POST | `/user/order` | 下单(body: itemId + spotId)，商品转"交易中" | P4 |
| POST | `/user/order/{id}/pay` | 模拟支付成功回调 | P4 |
| PUT  | `/user/order/{id}/confirm` | 买家确认收到货 → 已完成，商品转"已售出" | P4 |
| PUT  | `/user/order/{id}/cancel` | 买家取消，商品回"下架" | P4 |
| GET  | `/user/order/bought?page&pageSize` | 我买入的 | P4 |
| GET  | `/user/order/sold?page&pageSize` | 我卖出的 | P4 |

### 3.7 消息 message
| 方法 | 路径 | 说明 | 阶段 |
|------|------|------|------|
| GET  | `/user/message/unreadCount` | 未读数(导航栏红点) | P4 |
| GET  | `/user/message/page?page&pageSize` | 我的消息列表 | P4 |
| PUT  | `/user/message/read?id=` / `/readAll` | 标记已读 | P4 |

> WebSocket 推送通道 `ws://host/ws?token=<JWT>`：新订单实时提醒卖家（P4）

## 四、订单状态机（P4 的核心逻辑，先立此存照）

```
下单 ──► 0待支付 ──模拟支付──► 1待面交 ──买家confirm──► 2已完成
              │                    │
              │30min超时(Spring Task)│买家cancel
              ▼                    ▼
            3已取消 ◄──────────────┘

商品status联动: 下单→2交易中 | 完成→3已售出 | 取消→0下架(可重新上架)
```
