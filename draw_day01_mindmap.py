#!/usr/bin/env python3
"""
苍穹外卖 Day01 知识点思维导图生成器
输出: 保存到桌面 day01_mindmap.png
"""
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
import numpy as np

# ── 字体配置（中文支持）──
plt.rcParams['font.sans-serif'] = ['PingFang SC', 'Arial Unicode MS', 'SimHei', 'sans-serif']
plt.rcParams['axes.unicode_minus'] = False

# ── 颜色主题 ──
COLORS = {
    'root':      '#1a237e',   # 深蓝
    'tech':      '#0d47a1',   # 蓝
    'arch':      '#00695c',   # 青
    'login':     '#b71c1c',   # 红
    'mvc':       '#4a148c',   # 紫
    'config':    '#e65100',   # 橙
    'flow':      '#1b5e20',   # 绿
}
FACE_COLORS = {
    'root': '#ffffff',
    'tech': '#ffffff',
    'arch': '#ffffff',
    'login': '#ffffff',
    'mvc': '#ffffff',
    'config': '#ffffff',
    'flow': '#ffffff',
}

fig, ax = plt.subplots(figsize=(28, 18))
ax.set_xlim(0, 2800)
ax.set_ylim(0, 1800)
ax.set_aspect('equal')
ax.axis('off')
fig.patch.set_facecolor('#fafafa')

# ── 辅助函数 ──
def draw_box(ax, x, y, w, h, text, color, facecolor='white', fontsize=13,
             fontweight='normal', bbox_style='round,pad=0.4', linewidth=2):
    box = FancyBboxPatch((x - w/2, y - h/2), w, h,
                          boxstyle=bbox_style, facecolor=facecolor,
                          edgecolor=color, linewidth=linewidth,
                          zorder=3)
    ax.add_patch(box)
    ax.text(x, y, text, ha='center', va='center', fontsize=fontsize,
            fontweight=fontweight, color=color, zorder=4)
    return box

def draw_arrow(ax, x1, y1, x2, y2, color='#555555'):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='-|>', color=color, lw=2,
                                connectionstyle='arc3,rad=0'), zorder=2)

def draw_curved_arrow(ax, x1, y1, x2, y2, color='#555555'):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='-|>', color=color, lw=1.8,
                                connectionstyle='arc3,rad=0.15'), zorder=2)

# ════════════════════════════════════════════════════════════
# 中心根节点
# ════════════════════════════════════════════════════════════
draw_box(ax, 1400, 900, 380, 80, '苍穹外卖 Day01', COLORS['root'],
         facecolor=COLORS['root'], fontsize=22, fontweight='bold')

# ════════════════════════════════════════════════════════════
# 一级分支
# ════════════════════════════════════════════════════════════

branches = [
    ('技术栈\n依赖',      COLORS['tech'],     1400, 1450),
    ('Maven多模块架构',  COLORS['arch'],      550,  900),
    ('登录模块核心',     COLORS['login'],     2250, 900),
    ('Web MVC配置',      COLORS['mvc'],       1400, 350),
    ('配置体系',         COLORS['config'],    550,  450),
    ('开发流程',         COLORS['flow'],      2250, 450),
]

# 根节点到各分支的连线
for label, color, cx, cy in branches:
    draw_arrow(ax, 1400, 940, cx, cy - 50, color)
    # 分支标签圆角矩形
    words = label.split('\n')
    total_h = len(words) * 36 + 16
    draw_box(ax, cx, cy, 200, total_h, label, color,
             facecolor='white', fontsize=13, fontweight='bold')

# ════════════════════════════════════════════════════════════
# 分支1: 技术栈依赖
# ════════════════════════════════════════════════════════════
tech_items = [
    ('Spring Boot 2.7+', '#1565c0'),
    ('MyBatis +\nmybatis-spring-boot-starter', '#1976d2'),
    ('MySQL Connector / J', '#1976d2'),
    ('JJWT (jjwt 0.9.1)', '#1565c0'),
    ('Lombok', '#1565c0'),
    ('Knife4j\n(swagger增强版)', '#1976d2'),
    ('Druid 连接池', '#1565c0'),
    ('PageHelper 分页插件', '#1565c0'),
]
tx, ty = 1400, 1450
for i, (item, c) in enumerate(tech_items):
    ix = tx - 350 + i * 100
    iy = ty + 120 + (i % 2) * 55
    draw_box(ax, ix, iy, 160, 48, item, c, fontsize=10)
    draw_curved_arrow(ax, tx - 80, ty + 60, ix + 60, iy - 20, c)

# ════════════════════════════════════════════════════════════
# 分支2: Maven多模块架构
# ════════════════════════════════════════════════════════════
arch_items = [
    ('sky-common', '#00796b', '常量/异常/工具类\nResult/MessageConstant/JwtUtil\nBaseException/JwtProperties'),
    ('sky-pojo',   '#00695c', 'DTO/VO/Entity\nEmployeeLoginDTO\nEmployeeLoginVO/Employee'),
    ('sky-server', '#004d40', '启动类/配置/拦截器\nController/Service/Mapper'),
]
multi_items = [
    ('依赖方向', '#37474f', 'sky-pojo ← sky-common\nsky-server ← sky-pojo + sky-common'),
    ('包名规范', '#37474f', 'com.sky.*\n所有模块统一根包名'),
]
arch_x, arch_y = 550, 900
for i, (item, c, desc) in enumerate(arch_items):
    ix = ax - 250 + i * 170
    iy = arch_y + 120
    draw_box(ax, ix, iy, 150, 50, item, c, fontsize=12, fontweight='bold')
    draw_curved_arrow(ax, ax - 80, ay + 60, ix + 60, iy - 20, c)
    # 描述
    bbox = FancyBboxPatch((ix - 120, iy - 50), 240, 70,
                          boxstyle='round,pad=0.3', facecolor='#e0f2f1',
                          edgecolor=c, linewidth=1, alpha=0.7, zorder=2)
    ax.add_patch(bbox)
    ax.text(ix, iy - 15, desc, ha='center', va='center', fontsize=8.5,
            color='#004d40', zorder=3, linespacing=1.5)

for i, (item, c, desc) in enumerate(multi_items):
    ix = ax - 120 + i * 200
    iy = arch_y + 280
    draw_box(ax, ix, iy, 140, 45, item, c, fontsize=11, fontweight='bold')
    draw_curved_arrow(ax, ax - 150, ay + 200, ix + 50, iy + 20, c)
    bbox = FancyBboxPatch((ix - 100, iy - 45), 200, 60,
                          boxstyle='round,pad=0.3', facecolor='#eceff1',
                          edgecolor=c, linewidth=1, alpha=0.7, zorder=2)
    ax.add_patch(bbox)
    ax.text(ix, iy - 10, desc, ha='center', va='center', fontsize=8.5,
            color='#37474f', zorder=3, linespacing=1.4)

# ════════════════════════════════════════════════════════════
# 分支3: 登录模块核心（最重点）
# ════════════════════════════════════════════════════════════
login_x, login_y = 2250, 900

# Controller
draw_box(ax, login_x - 300, login_y + 100, 200, 50,
         'EmployeeController', '#c62828', fontsize=11, fontweight='bold')
draw_curved_arrow(ax, login_x + 80, login_y + 60, login_x - 180, login_y + 120, '#c62828')
draw_box(ax, login_x - 300, login_y + 10, 200, 45,
         '@RestController\n@RequestMapping("/admin/employee")', '#ef5350', fontsize=9)
draw_arrow(ax, login_x - 300, login_y + 130, login_x - 300, login_y + 55, '#c62828')

# login方法
login_steps = [
    ('POST /login', '#d32f2f'),
    ('@RequestBody\nEmployeeLoginDTO', '#e53935'),
    ('employeeService\n.login(dto)', '#c62828'),
    ('return Result<\nEmployeeLoginVO>', '#b71c1c'),
]
for i, (s, c) in enumerate(login_steps):
    sx = login_x - 450 + i * 120
    sy = login_y - 80
    draw_box(ax, sx, sy, 115, 45, s, c, fontsize=9)
    if i < len(login_steps) - 1:
        ax.annotate('', xy=(sx + 60, sy), xytext=(sx + 55, sy),
                    arrowprops=dict(arrowstyle='->', color=c, lw=1.5), zorder=2)

# Service层
draw_box(ax, login_x + 150, login_y + 100, 200, 50,
         'EmployeeServiceImpl', '#c62828', fontsize=11, fontweight='bold')
draw_curved_arrow(ax, login_x + 80, login_y + 60, login_x + 230, login_y + 120, '#c62828')

service_steps = [
    ('1. 查数据库\nemployeeMapper.getByUsername()', '#e53935'),
    ('2. 判空\n→ AccountNotFoundException', '#ef5350'),
    ('3. 比密码\n→ PasswordErrorException', '#e53935'),
    ('4. 判状态\n→ AccountLockedException', '#c62828'),
]
for i, (s, c) in enumerate(service_steps):
    sx = login_x + 80 + i * 105
    sy = login_y - 80
    draw_box(ax, sx, sy, 100, 50, s, c, fontsize=8.5)

# JWT生成
jwt_box = FancyBboxPatch((login_x + 100, login_y - 200), 350, 90,
                          boxstyle='round,pad=0.4', facecolor='#ffebee',
                          edgecolor='#c62828', linewidth=2, zorder=2)
ax.add_patch(jwt_box)
ax.text(login_x + 275, login_y - 155, 'JWT 令牌生成', ha='center', va='center',
        fontsize=12, fontweight='bold', color='#b71c1c', zorder=3)
ax.text(login_x + 275, login_y - 178, 'JwtUtil.createJWT(secretKey, ttl, claims)',
        ha='center', va='center', fontsize=9, color='#c62828', zorder=3)
ax.text(login_x + 275, login_y - 198, 'claims = {empId: employee.getId()}',
        ha='center', va='center', fontsize=9, color='#c62828', zorder=3)
draw_arrow(ax, login_x + 250, login_y - 55, login_x + 275, login_y - 105, '#c62828')

# ════════════════════════════════════════════════════════════
# 分支4: Web MVC 配置
# ════════════════════════════════════════════════════════════
mvc_x, mvc_y = 1400, 350

draw_box(ax, mvc_x - 300, mvc_y + 80, 200, 50,
         'WebMvcConfiguration', '#6a1b9a', fontsize=11, fontweight='bold')
draw_arrow(ax, mvc_x - 80, mvc_y + 60, mvc_x - 180, mvc_y + 100, '#6a1b9a')

mvc_items = [
    ('注册拦截器\naddInterceptors', '#8e24aa',
     '拦截路径: /admin/**\n排除: /admin/employee/login\n(登录接口不拦截)'),
    ('Knife4j接口文档\n@Bean Docket', '#7b1fa2',
     'DocumentationType.SWAGGER_2\n访问: http://localhost:8080/doc.html'),
    ('静态资源映射\naddResourceHandlers', '#6a1b9a',
     '/doc.html → classpath:/META-INF/resources/\n/webjars/** → classpath:...'),
    ('全局异常处理\nGlobalExceptionHandler', '#8e24aa',
     '@RestControllerAdvice\n统一捕获业务异常(BaseException)\n返回 Result.error(msg)'),
]
for i, (title, c, desc) in enumerate(mvc_items):
    mx = mvc_x - 350 + i * 200
    my = mvc_y - 100
    draw_box(ax, mx, my, 185, 45, title, c, fontsize=10, fontweight='bold')
    draw_curved_arrow(ax, mvc_x - 200, mvc_y + 30, mx + 80, my + 20, c)
    bbox = FancyBboxPatch((mx - 100, my - 80), 200, 65,
                          boxstyle='round,pad=0.3', facecolor='#f3e5f5',
                          edgecolor=c, linewidth=1, alpha=0.8, zorder=2)
    ax.add_patch(bbox)
    ax.text(mx, my - 50, desc, ha='center', va='center', fontsize=8,
            color='#4a148c', zorder=3, linespacing=1.4)

# ════════════════════════════════════════════════════════════
# 分支5: 配置体系
# ════════════════════════════════════════════════════════════
cfg_x, cfg_y = 550, 450

draw_box(ax, cfg_x, cfg_y + 80, 200, 50,
         'application.yml', '#bf360c', fontsize=12, fontweight='bold')
draw_arrow(ax, cfg_x + 80, cfg_y + 60, cfg_x, cfg_y + 100, '#bf360c')

cfg_items = [
    ('sky.jwt配置', '#e65100',
     'admin-secret-key: itcast\nadmin-ttl: 7200000 (2h)\nadmin-token-name: token'),
    ('spring.datasource', '#f57c00',
     'druid连接池\ndriver-class-name\nurl / username / password\n(环境变量占位符)'),
    ('mybatis配置', '#ef6c00',
     'mapper-locations: classpath:mapper/*.xml\ntype-aliases-package: com.sky.entity\nmap-underscore-to-camel-case: true'),
]
for i, (title, c, desc) in enumerate(cfg_items):
    cx = cfg_x - 220 + i * 200
    cy = cfg_y - 100
    draw_box(ax, cx, cy, 185, 45, title, c, fontsize=10, fontweight='bold')
    draw_curved_arrow(ax, cfg_x - 80, cfg_y + 30, cx + 80, cy + 20, c)
    bbox = FancyBboxPatch((cx - 100, cy - 70), 200, 60,
                          boxstyle='round,pad=0.3', facecolor='#fff3e0',
                          edgecolor=c, linewidth=1, alpha=0.8, zorder=2)
    ax.add_patch(bbox)
    ax.text(cx, cy - 40, desc, ha='center', va='center', fontsize=8,
            color='#bf360c', zorder=3, linespacing=1.3)

# ════════════════════════════════════════════════════════════
# 分支6: 开发流程
# ════════════════════════════════════════════════════════════
flow_x, flow_y = 2250, 450

draw_box(ax, flow_x, flow_y + 80, 220, 50,
         'Day01 开发流程', '#2e7d32', fontsize=12, fontweight='bold')
draw_arrow(ax, flow_x + 80, flow_y + 60, flow_x, flow_y + 100, '#2e7d32')

flow_steps = [
    ('1. 建库建表', '#43a047', '创建sky_take_out数据库\nemployee表（账号/密码/状态）'),
    ('2. Maven多模块\n搭建骨架', '#66bb6a', '创建父pom.xml\nsky-common / sky-pojo / sky-server\n三模块及各自pom'),
    ('3. 编写Entity\n& DTO & VO', '#43a047', 'Employee实体类\nEmployeeLoginDTO / EmployeeLoginVO'),
    ('4. 编写Mapper\n& Service', '#66bb6a', 'EmployeeMapper (继承BaseMapper)\nEmployeeServiceImpl (登录业务逻辑)\n全局异常处理类'),
    ('5. 编写Controller\n& 拦截器', '#43a047', 'EmployeeController (@RestController)\nJwtTokenAdminInterceptor'),
    ('6. Knife4j\n配置文档', '#66bb6a', 'WebMvcConfiguration注册Docket\n访问http://localhost:8080/doc.html'),
    ('7. 前后端联调\n接口测试', '#43a047', '用Postman或Knife4j测试/login接口\n验证JWT Token签发'),
]
for i, (title, c, desc) in enumerate(flow_steps):
    fx = flow_x - 280 + (i % 3) * 210
    fy = flow_y - 80 + (i // 3) * 130
    draw_box(ax, fx, fy, 195, 45, title, c, fontsize=10, fontweight='bold')
    draw_curved_arrow(ax, flow_x - 100, flow_y + 30, fx + 90, fy + 20, c)
    bbox = FancyBboxPatch((fx - 90, fy - 65), 180, 55,
                          boxstyle='round,pad=0.3', facecolor='#e8f5e9',
                          edgecolor=c, linewidth=1, alpha=0.8, zorder=2)
    ax.add_patch(bbox)
    ax.text(fx, fy - 35, desc, ha='center', va='center', fontsize=7.5,
            color='#1b5e20', zorder=3, linespacing=1.3)

# ════════════════════════════════════════════════════════════
# 标题栏
# ════════════════════════════════════════════════════════════
title_box = FancyBboxPatch((600, 1680), 1600, 70,
                            boxstyle='round,pad=0.5', facecolor='#1a237e',
                            edgecolor='#283593', linewidth=2, zorder=5)
ax.add_patch(title_box)
ax.text(1400, 1715, '苍穹外卖 · Day01 知识点思维导图', ha='center', va='center',
        fontsize=24, fontweight='bold', color='white', zorder=6)

# ════════════════════════════════════════════════════════════
# 图例
# ════════════════════════════════════════════════════════════
legend_y = 200
legend_items = [
    ('技术栈', '#0d47a1'),
    ('多模块架构', '#00695c'),
    ('登录模块', '#b71c1c'),
    ('MVC配置', '#4a148c'),
    ('配置体系', '#e65100'),
    ('开发流程', '#1b5e20'),
]
for i, (label, c) in enumerate(legend_items):
    lx = 80 + i * 200
    legend_box = FancyBboxPatch((lx - 10, legend_y - 15), 120, 30,
                                 boxstyle='round,pad=0.2', facecolor=c,
                                 edgecolor=c, linewidth=1, zorder=5)
    ax.add_patch(legend_box)
    ax.text(lx + 50, legend_y, label, ha='center', va='center',
            fontsize=10, color='white', fontweight='bold', zorder=6)

ax.text(1400, 150, '黑马程序员 · Spring Boot Web案例 · Day01 环境搭建与登录功能',
        ha='center', va='center', fontsize=11, color='#757575', style='italic')

plt.tight_layout(pad=1.0)
output_path = '/Users/timwong/Desktop/day01_mindmap.png'
fig.savefig(output_path, dpi=150, bbox_inches='tight', facecolor='#fafafa')
print(f'✅ 思维导图已保存到: {output_path}')
plt.close()
