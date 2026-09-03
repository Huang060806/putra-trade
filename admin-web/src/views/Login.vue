<template>
  <div style="height:100vh; display:flex; align-items:center; justify-content:center; background:#f0f2f5">
    <el-card style="width:400px">
      <h2 style="text-align:center; margin-top:0">Putra Trade 管理后台</h2>
      <el-form :model="form" @keyup.enter="submit">
        <el-form-item><el-input v-model="form.account" placeholder="管理员账号" /></el-form-item>
        <el-form-item><el-input v-model="form.password" type="password" placeholder="密码" show-password /></el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click="submit">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api'
import { setToken } from '../utils/auth'

const router = useRouter()
const form = reactive({ account: 'admin', password: '' })
const loading = ref(false)

const submit = async () => {
  loading.value = true
  try {
    const data = await request.post('/admin/employee/login', form)
    setToken(data.token)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>
