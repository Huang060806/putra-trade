-- ============================================================
-- Putra Trade · UPM 校园二手交易平台
-- 数据库初始化脚本 v1.0 (最终版)
-- MySQL 8.x+ / InnoDB / utf8mb4_unicode_ci
-- 执行: mysql -u root -p < putra_trade.sql
-- ============================================================

DROP DATABASE IF EXISTS putra_trade;
CREATE DATABASE IF NOT EXISTS putra_trade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE putra_trade;

-- ------------------------------------------------------------
-- 1. 管理员表 employee
-- ------------------------------------------------------------
DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)  NOT NULL                COMMENT '姓名',
    username    VARCHAR(32)  NOT NULL                COMMENT '登录账号',
    password    VARCHAR(72)  NOT NULL                COMMENT '密码(BCrypt)',
    phone       VARCHAR(20)                          COMMENT '手机号',
    status      TINYINT      NOT NULL DEFAULT 1      COMMENT '账号状态: 1启用 0禁用',
    create_time DATETIME     NOT NULL                COMMENT '创建时间',
    update_time DATETIME     NOT NULL                COMMENT '最后修改时间',
    create_user BIGINT                               COMMENT '创建人id',
    update_user BIGINT                               COMMENT '修改人id',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='平台管理员';

-- ------------------------------------------------------------
-- 2. 学生用户表 member
-- ------------------------------------------------------------
DROP TABLE IF EXISTS member;
CREATE TABLE member (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    student_no  VARCHAR(20)  NOT NULL                COMMENT '学号',
    email       VARCHAR(64)  NOT NULL                COMMENT 'UPM邮箱',
    password    VARCHAR(72)  NOT NULL                COMMENT '密码(BCrypt)',
    nickname    VARCHAR(32)  NOT NULL                COMMENT '昵称',
    avatar      VARCHAR(500)                         COMMENT '头像URL',
    gender      TINYINT      NOT NULL DEFAULT 0      COMMENT '性别: 0未知 1男 2女',
    phone       VARCHAR(20)                          COMMENT '手机号(选填)',
    wechat      VARCHAR(64)                          COMMENT '微信号(点"我想要"后向买家展示)',
    whatsapp    VARCHAR(20)                          COMMENT 'WhatsApp号(选填)',
    dorm_area   VARCHAR(64)                          COMMENT '宿舍区域(如 KMR/College 10/DKP)',
    ban_publish TINYINT      NOT NULL DEFAULT 0      COMMENT '禁止发布: 1禁止 0正常',
    ban_chat    TINYINT      NOT NULL DEFAULT 0      COMMENT '禁止私聊: 1禁止 0正常',
    status      TINYINT      NOT NULL DEFAULT 1      COMMENT '状态: 1正常 0封禁',
    create_time DATETIME     NOT NULL                COMMENT '注册时间',
    update_time DATETIME     NOT NULL                COMMENT '最后修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_no (student_no),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB COMMENT='学生用户';

-- ------------------------------------------------------------
-- 3. 商品分类表 category
-- ------------------------------------------------------------
DROP TABLE IF EXISTS category;
CREATE TABLE category (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32) NOT NULL                COMMENT '分类名称',
    sort        INT         NOT NULL DEFAULT 0      COMMENT '排序(越小越靠前)',
    create_time DATETIME    NOT NULL                COMMENT '创建时间',
    update_time DATETIME    NOT NULL                COMMENT '最后修改时间',
    create_user BIGINT                              COMMENT '创建人id',
    update_user BIGINT                              COMMENT '修改人id',
    PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='商品分类';

-- ------------------------------------------------------------
-- 4. 商品表 item
-- ------------------------------------------------------------
DROP TABLE IF EXISTS item;
CREATE TABLE item (
    id              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    title           VARCHAR(64)   NOT NULL                COMMENT '商品标题',
    category_id     BIGINT        NOT NULL                COMMENT '所属分类id',
    seller_id       BIGINT        NOT NULL                COMMENT '卖家id(=member.id)',
    price           DECIMAL(10,2) NOT NULL                COMMENT '售价(RM)',
    original_price  DECIMAL(10,2)                         COMMENT '原价(RM)',
    condition_level TINYINT       NOT NULL DEFAULT 3      COMMENT '成色: 1全新未拆 2几乎全新 3轻微使用痕迹 4明显使用痕迹',
    cover           VARCHAR(500)                          COMMENT '封面图URL',
    description     TEXT                                  COMMENT '详细描述',
    campus_area     VARCHAR(64)                           COMMENT '交货地点标签(如 KMR/College 10/DKP/Library)',
    status          TINYINT       NOT NULL DEFAULT 4      COMMENT '状态: 1在售 2预订/交易中 3已售出 0已下架 4待审核(敏感词命中)',
    audit_remark    VARCHAR(255)                          COMMENT '审核备注(命中词/驳回原因)',
    want_count      INT           NOT NULL DEFAULT 0      COMMENT '想要人数(冗余,用于热度排序)',
    view_count      INT           NOT NULL DEFAULT 0      COMMENT '浏览量',
    create_time     DATETIME      NOT NULL                COMMENT '发布时间',
    update_time     DATETIME      NOT NULL                COMMENT '最后修改时间',
    create_user     BIGINT                                COMMENT '创建人id(公共字段填充)',
    update_user     BIGINT                                COMMENT '修改人id(公共字段填充)',
    PRIMARY KEY (id),
    KEY idx_category (category_id),
    KEY idx_seller (seller_id),
    KEY idx_status_create (status, create_time),
    KEY idx_status_want (status, want_count)
) ENGINE=InnoDB COMMENT='商品';

-- ------------------------------------------------------------
-- 5. 商品图片表 item_image
-- ------------------------------------------------------------
DROP TABLE IF EXISTS item_image;
CREATE TABLE item_image (
    id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    item_id BIGINT       NOT NULL                COMMENT '商品id',
    url     VARCHAR(500) NOT NULL                COMMENT '图片URL',
    sort    INT          NOT NULL DEFAULT 0      COMMENT '展示顺序',
    PRIMARY KEY (id),
    KEY idx_item (item_id)
) ENGINE=InnoDB COMMENT='商品图册(封面之外的补充图)';

-- ------------------------------------------------------------
-- 6. 收藏表 favorite
-- ------------------------------------------------------------
DROP TABLE IF EXISTS favorite;
CREATE TABLE favorite (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    member_id   BIGINT   NOT NULL                COMMENT '用户id',
    item_id     BIGINT   NOT NULL                COMMENT '商品id',
    create_time DATETIME NOT NULL                COMMENT '收藏时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_item (member_id, item_id)
) ENGINE=InnoDB COMMENT='收藏夹';

-- ------------------------------------------------------------
-- 7. 面交地点表 meet_spot
-- ------------------------------------------------------------
DROP TABLE IF EXISTS meet_spot;
CREATE TABLE meet_spot (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    member_id     BIGINT       NOT NULL                COMMENT '所属用户id',
    contact_name  VARCHAR(32)  NOT NULL                COMMENT '联系人姓名',
    contact_phone VARCHAR(20)  NOT NULL                COMMENT '联系电话',
    spot_name     VARCHAR(128) NOT NULL                COMMENT '面交地点',
    remark        VARCHAR(200)                         COMMENT '备注',
    is_default    TINYINT      NOT NULL DEFAULT 0      COMMENT '是否默认: 1是 0否',
    create_time   DATETIME     NOT NULL                COMMENT '创建时间',
    update_time   DATETIME     NOT NULL                COMMENT '最后修改时间',
    PRIMARY KEY (id),
    KEY idx_member (member_id)
) ENGINE=InnoDB COMMENT='常用面交地点';

-- ------------------------------------------------------------
-- 8. 订单表 orders
-- ------------------------------------------------------------
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no         VARCHAR(50)   NOT NULL                COMMENT '订单号',
    buyer_id         BIGINT        NOT NULL                COMMENT '买家id',
    seller_id        BIGINT        NOT NULL                COMMENT '卖家id(冗余)',
    item_id          BIGINT        NOT NULL                COMMENT '商品id',
    item_title       VARCHAR(64)   NOT NULL                COMMENT '商品标题快照',
    price            DECIMAL(10,2) NOT NULL                COMMENT '成交价快照(RM)',
    meet_spot_info   VARCHAR(255)  NOT NULL                COMMENT '面交信息快照',
    status           TINYINT       NOT NULL DEFAULT 0      COMMENT '状态: 0待支付 1待面交 2已完成 3已取消',
    pay_method       TINYINT       NOT NULL DEFAULT 1      COMMENT '支付方式: 1模拟支付',
    cancel_reason    VARCHAR(255)                          COMMENT '取消原因',
    order_time       DATETIME      NOT NULL                COMMENT '下单时间',
    pay_time         DATETIME                              COMMENT '支付时间',
    complete_time    DATETIME                              COMMENT '完成时间',
    cancel_time      DATETIME                              COMMENT '取消时间',
    create_time      DATETIME      NOT NULL                COMMENT '创建时间',
    update_time      DATETIME      NOT NULL                COMMENT '最后修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_buyer (buyer_id),
    KEY idx_seller (seller_id),
    KEY idx_status (status)
) ENGINE=InnoDB COMMENT='订单';

-- ------------------------------------------------------------
-- 9. 站内消息表 message
-- ------------------------------------------------------------
DROP TABLE IF EXISTS message;
CREATE TABLE message (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    receiver_id BIGINT       NOT NULL                COMMENT '接收人id',
    type        TINYINT      NOT NULL                COMMENT '类型: 1新订单提醒 2订单状态变更 3平台通知 4有人想要',
    content     VARCHAR(500) NOT NULL                COMMENT '消息内容',
    order_id    BIGINT                               COMMENT '关联订单id',
    is_read     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否已读: 1已读 0未读',
    create_time DATETIME     NOT NULL                COMMENT '发送时间',
    PRIMARY KEY (id),
    KEY idx_receiver_read (receiver_id, is_read)
) ENGINE=InnoDB COMMENT='站内消息';

-- ------------------------------------------------------------
-- 10. 想要表 want
-- ------------------------------------------------------------
DROP TABLE IF EXISTS want;
CREATE TABLE want (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    member_id   BIGINT   NOT NULL                COMMENT '用户id(买家)',
    item_id     BIGINT   NOT NULL                COMMENT '商品id',
    create_time DATETIME NOT NULL                COMMENT '想要时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_item (member_id, item_id),
    KEY idx_item (item_id)
) ENGINE=InnoDB COMMENT='想要列表(点"我想要"解锁卖家联系方式)';

-- ------------------------------------------------------------
-- 11. 举报表 report
-- ------------------------------------------------------------
DROP TABLE IF EXISTS report;
CREATE TABLE report (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    reporter_id   BIGINT       NOT NULL                COMMENT '举报人id',
    target_type   TINYINT      NOT NULL                COMMENT '举报对象: 1商品 2用户',
    target_id     BIGINT       NOT NULL                COMMENT '被举报对象id(商品id或用户id)',
    reason        VARCHAR(500) NOT NULL                COMMENT '举报原因',
    images        VARCHAR(1000)                        COMMENT '凭证图片(多图逗号分隔,选填)',
    status        TINYINT      NOT NULL DEFAULT 0      COMMENT '处理状态: 0待处理 1已处理 2已驳回',
    handle_result VARCHAR(500)                         COMMENT '处理结果说明',
    handler_id    BIGINT                               COMMENT '处理人id(employee.id)',
    handle_time   DATETIME                             COMMENT '处理时间',
    create_time   DATETIME     NOT NULL                COMMENT '举报时间',
    update_time   DATETIME     NOT NULL                COMMENT '最后修改时间',
    PRIMARY KEY (id),
    KEY idx_type_status (target_type, status),
    KEY idx_reporter (reporter_id)
) ENGINE=InnoDB COMMENT='举报(帖子/用户)';

-- ------------------------------------------------------------
-- 12. 敏感词表 sensitive_word
-- ------------------------------------------------------------
DROP TABLE IF EXISTS sensitive_word;
CREATE TABLE sensitive_word (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    word        VARCHAR(64) NOT NULL                COMMENT '敏感词',
    type        TINYINT     NOT NULL DEFAULT 1      COMMENT '类别: 1政治 2色情 3违禁品(烟酒/药品等)',
    create_time DATETIME    NOT NULL                COMMENT '创建时间',
    update_time DATETIME    NOT NULL                COMMENT '最后修改时间',
    create_user BIGINT                              COMMENT '创建人id',
    PRIMARY KEY (id),
    UNIQUE KEY uk_word (word)
) ENGINE=InnoDB COMMENT='敏感词库(发布时DFA自动过滤)';

-- ------------------------------------------------------------
-- 13. 私聊消息表 chat_message
-- ------------------------------------------------------------
DROP TABLE IF EXISTS chat_message;
CREATE TABLE chat_message (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    sender_id   BIGINT       NOT NULL                COMMENT '发送人id',
    receiver_id BIGINT       NOT NULL                COMMENT '接收人id',
    item_id     BIGINT                                COMMENT '关联商品id(从商品页发起时记录)',
    content     VARCHAR(500) NOT NULL                COMMENT '消息内容',
    is_read     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否已读: 1已读 0未读',
    create_time DATETIME     NOT NULL                COMMENT '发送时间',
    PRIMARY KEY (id),
    KEY idx_pair (sender_id, receiver_id, create_time),
    KEY idx_receiver_read (receiver_id, is_read)
) ENGINE=InnoDB COMMENT='一对一私聊消息';

-- ============================================================
-- 初始数据
-- ============================================================

-- 管理员: admin / admin123
INSERT INTO employee (name, username, password, phone, status, create_time, update_time) VALUES
('平台管理员', 'admin', '$2b$12$vevFfp.YkpNar5U5kyVNFekFpX3eWHsFdtI90xWhJeFKEtoKOMrhy', '012-3456789', 1, NOW(), NOW());
-- BCrypt 哈希对应的明文密码: admin123

-- 测试学生账号 (密码均为 123456，BCrypt 哈希同下)
-- password: $2b$12$wmpwlcCS/ppdHAqNTiN3guuses1PyBSadWrrEiN/eqUY0rsLDTXCy
INSERT INTO member (student_no, email, password, nickname, gender, phone, wechat, whatsapp, dorm_area, status, create_time, update_time) VALUES
('BC210001', 'BC210001@student.upm.edu.my', '$2b$12$wmpwlcCS/ppdHAqNTiN3guuses1PyBSadWrrEiN/eqUY0rsLDTXCy', '张伟', 1, '011-11112222', 'zhangwei_upm', '011-11112222', 'KMR', 1, NOW(), NOW()),
('BC210002', 'BC210002@student.upm.edu.my', '$2b$12$wmpwlcCS/ppdHAqNTiN3guuses1PyBSadWrrEiN/eqUY0rsLDTXCy', '李爽', 2, '012-22223333', 'lishuang88', NULL, 'College 10', 1, NOW(), NOW()),
('BC210003', 'BC210003@student.upm.edu.my', '$2b$12$wmpwlcCS/ppdHAqNTiN3guuses1PyBSadWrrEiN/eqUY0rsLDTXCy', '王芳', 2, '013-33334444', 'wangfang_upm', '013-33334444', 'DKP', 1, NOW(), NOW()),
('BC210004', 'BC210004@student.upm.edu.my', '$2b$12$wmpwlcCS/ppdHAqNTiN3guuses1PyBSadWrrEiN/eqUY0rsLDTXCy', '刘德', 1, '014-44445555', NULL, '014-44445555', 'Library旁KTAG', 1, NOW(), NOW());

-- 商品分类
INSERT INTO category (name, sort, create_time, update_time, create_user, update_user) VALUES
('课本/讲义',      1, NOW(), NOW(), 1, 1),
('数码电子',      2, NOW(), NOW(), 1, 1),
('宿舍家居',      3, NOW(), NOW(), 1, 1),
('代步工具',      4, NOW(), NOW(), 1, 1),
('服饰美妆',      5, NOW(), NOW(), 1, 1),
('校园服务/兼职', 6, NOW(), NOW(), 1, 1),
('其他闲置',     99, NOW(), NOW(), 1, 1);

-- 测试商品 (封面用 emoji 占位，演示用; campus_area 为交货地点标签)
INSERT INTO item (title, category_id, seller_id, price, original_price, condition_level, status, campus_area, want_count, view_count, create_time, update_time, create_user, update_user) VALUES
('iPad 第9代 64G WiFi', 2, 1, 850.00, 1299.00, 2, 1, 'KMR',        3, 128, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), 1, 1),
('高数上册 第七版',      1, 2, 20.00,  45.00,  3, 1, 'Library',    5,  89, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), 1, 1),
('尤尼克斯羽毛球拍',     3, 3, 180.00, 320.00, 3, 1, 'College 10', 2,  56, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), 1, 1),
('Nike Air Max 270',     5, 1, 150.00, 280.00, 3, 2, 'KMR',        1, 201, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), 1, 1),
('机械键盘 Keychron K2', 2, 4, 220.00, 350.00, 2, 1, 'Library',    4,  67, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), 1, 1),
('线性代数同济版',       1, 2, 15.00,  38.00,  3, 1, 'DKP',        6,  43, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), 1, 1),
('折叠自行车 9成新',     4, 3, 260.00, 480.00, 3, 1, 'College 10', 8,  78, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW(), 1, 1),
('Yoga Mat 瑜伽垫',      3, 4, 35.00,  68.00,  2, 1, 'KMR',        1,  34, DATE_SUB(NOW(), INTERVAL 12 HOUR), NOW(), 1, 1),
('雅思真题集 4-18',      1, 1, 25.00,  52.00,  4, 1, 'DKP',        2,  91, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW(), 1, 1),
('蓝牙音箱 JBL Flip 6',  2, 4, 320.00, 499.00, 2, 3, 'Library',    0, 156, DATE_SUB(NOW(), INTERVAL 9 DAY), NOW(), 1, 1);

-- 商品图片 (每个商品2-4张)
INSERT INTO item_image (item_id, url, sort) VALUES
(1, 'https://picsum.photos/seed/ipad1/400/400', 1), (1, 'https://picsum.photos/seed/ipad2/400/400', 2), (1, 'https://picsum.photos/seed/ipad3/400/400', 3),
(2, 'https://picsum.photos/seed/math1/400/400', 1), (2, 'https://picsum.photos/seed/math2/400/400', 2),
(3, 'https://picsum.photos/seed/rugby1/400/400', 1),
(4, 'https://picsum.photos/seed/nike1/400/400', 1), (4, 'https://picsum.photos/seed/nike2/400/400', 2), (4, 'https://picsum.photos/seed/nike3/400/400', 3),
(5, 'https://picsum.photos/seed/keyboard1/400/400', 1), (5, 'https://picsum.photos/seed/keyboard2/400/400', 2),
(6, 'https://picsum.photos/seed/algebra1/400/400', 1),
(7, 'https://picsum.photos/seed/yonex1/400/400', 1), (7, 'https://picsum.photos/seed/yonex2/400/400', 2),
(8, 'https://picsum.photos/seed/yoga1/400/400', 1),
(9, 'https://picsum.photos/seed/tongji1/400/400', 1), (9, 'https://picsum.photos/seed/tongji2/400/400', 2),
(10, 'https://picsum.photos/seed/jbl1/400/400', 1), (10, 'https://picsum.photos/seed/jbl2/400/400', 2), (10, 'https://picsum.photos/seed/jbl3/400/400', 3);

-- 面交地点
INSERT INTO meet_spot (member_id, contact_name, contact_phone, spot_name, remark, is_default, create_time, update_time) VALUES
(1, '张伟', '011-11112222', 'K17 宿管门口', '工作日晚上6点后可约',   1, NOW(), NOW()),
(1, '张伟', '011-11112222', '工学院 1楼大厅', '',                     0, NOW(), NOW()),
(2, '李爽', '012-22223333', '主图书馆 1楼咖啡角', '上课间隙可面交',     1, NOW(), NOW()),
(3, '王芳', '013-33334444', 'K16 篮球场旁', '下午4点后有空',           1, NOW(), NOW()),
(4, '刘德', '014-44445555', '图书馆 1楼大厅', '平日中午/晚上均可',      1, NOW(), NOW());

-- 收藏记录
INSERT INTO favorite (member_id, item_id, create_time) VALUES
(1, 3, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 4, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 2, NOW()),
(3, 8, NOW()),
(4, 6, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 订单 (1笔已完成, 1笔待支付, 1笔已取消, 演示状态机)
INSERT INTO orders (order_no, buyer_id, seller_id, item_id, item_title, price, meet_spot_info, status, pay_method, cancel_reason, order_time, pay_time, complete_time, cancel_time, create_time, update_time) VALUES
('PT20260823001', 2, 1, 4, 'Nike Air Max 270', 150.00, 'K17宿管门口 | 张伟 011-11112222', 2, 1, NULL, NOW(), NOW(), NOW(), NULL, NOW(), NOW()),
('PT20260823002', 3, 4, 5, '机械键盘 Keychron K2', 220.00, '图书馆1楼大厅 | 刘德 014-44445555', 0, 1, NULL, DATE_SUB(NOW(), INTERVAL 20 MINUTE), NULL, NULL, NULL, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
('PT20260822003', 4, 2, 2, '高数上册 第七版', 20.00, '主图书馆1楼咖啡角 | 李爽 012-22223333', 3, 1, '买家取消: 找到更便宜的', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 站内消息
INSERT INTO message (receiver_id, type, content, order_id, is_read, create_time) VALUES
(1, 1, '你的商品「iPad 第9代 64G」被张伟下单了，请尽快确认面交安排。', 1, 0, NOW()),
(3, 1, '你的商品「高数上册 第七版」被王芳收藏了，有人对它有浓厚兴趣！', NULL, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 3, '您的商品「高数上册 第七版」因违反平台规定已被下架。原因：商品信息不符。如需申诉请通过站内信联系客服。', NULL, 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 1, '你的商品「机械键盘 Keychron K2」被刘德下单了。', 2, 0, NOW());

-- 想要记录
INSERT INTO want (member_id, item_id, create_time) VALUES
(2, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 2, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 6, NOW()),
(2, 7, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 7, NOW());

-- 举报记录 (1条待处理的商品举报, 1条已处理的用户举报)
INSERT INTO report (reporter_id, target_type, target_id, reason, status, handle_result, handler_id, handle_time, create_time, update_time) VALUES
(2, 1, 4, '疑似假货，卖家拒绝提供购买凭证', 0, NULL, NULL, NULL, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(1, 2, 4, '私聊中使用侮辱性语言', 1, '已禁止该用户私聊7天', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 敏感词库 (示例, 后台可增删)
INSERT INTO sensitive_word (word, type, create_time, update_time, create_user) VALUES
('香烟', 3, NOW(), NOW(), 1),
('酒水', 3, NOW(), NOW(), 1),
('处方药', 3, NOW(), NOW(), 1);
