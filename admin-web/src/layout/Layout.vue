<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #001529">
      <div style="color:#fff; text-align:center; padding:20px 0; font-size:18px; font-weight:bold">
        Putra Trade 后台
      </div>
      <el-menu :default-active="$route.path" router background-color="#001529" text-color="#a6adb4" active-text-color="#fff">
        <el-menu-item index="/dashboard">数据看板</el-menu-item>
        <el-menu-item index="/items">商品管理</el-menu-item>
        <el-menu-item index="/reports">举报审核</el-menu-item>
        <el-menu-item index="/members">用户管理</el-menu-item>
        <el-menu-item index="/categories">分类管理</el-menu-item>
        <el-menu-item index="/words">敏感词库</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid #eee">
        <span>{{ $route.meta.title }}</span>
        <div>
          <el-tag :type="platformStatus === 1 ? 'success' : 'danger'" style="margin-right:12px">
            {{ platformStatus === 1 ? '平台运行中' : '维护中' }}
          </el-tag>
          <el-switch :model-value="platformStatus === 1" @change="togglePlatform" active-text="运营开关" style="margin-right:16px" />
          <el-button text @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api'
import { clearToken } from '../utils/auth'

const router = useRouter()
const platformStatus = ref(1)

onMounted(async () => {
  platformStatus.value = await request.get('/admin/platform/status')
})

const togglePlatform = async (on) => {
  await request.put(`/admin/platform/${on ? 1 : 0}`)
  platformStatus.value = on ? 1 : 0
}

const logout = () => {
  clearToken()
  router.push('/login')
}
</script>
