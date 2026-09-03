<template>
  <div style="padding:60px 24px 0">
    <h2 style="text-align:center">Putra Trade</h2>
    <p style="text-align:center; color:#999">UPM 校园二手交易平台</p>
    <van-form @submit="submit">
      <van-field v-model="form.account" label="账号" placeholder="学号或 UPM 邮箱" :rules="[{ required: true, message: '请输入账号' }]" />
      <van-field v-model="form.password" type="password" label="密码" placeholder="密码" :rules="[{ required: true, message: '请输入密码' }]" />
      <div style="margin:24px 16px 12px">
        <van-button round block type="primary" native-type="submit" :loading="loading">登录</van-button>
      </div>
      <div style="text-align:center">
        <router-link to="/register" style="color:#1989fa">没有账号？去注册</router-link>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../api'
import { setToken, setUser } from '../utils/auth'

const router = useRouter()
const form = reactive({ account: '', password: '' })
const loading = ref(false)

const submit = async () => {
  loading.value = true
  try {
    const data = await request.post('/user/auth/login', form)
    setToken(data.token)
    setUser(data)
    showToast('登录成功')
    router.replace('/')
  } finally {
    loading.value = false
  }
}
</script>
