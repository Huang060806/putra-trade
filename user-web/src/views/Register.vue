<template>
  <div style="padding:40px 24px 0">
    <h2 style="text-align:center">注册</h2>
    <van-form @submit="submit">
      <van-field v-model="form.studentNo" label="学号" placeholder="如 BC123456" :rules="[{ required: true, message: '请输入学号' }]" />
      <van-field v-model="form.email" label="UPM邮箱" placeholder="BC123456@student.upm.edu.my" :rules="[{ required: true, message: '请输入 UPM 邮箱' }]" />
      <van-field v-model="form.nickname" label="昵称" placeholder="选填，默认学号" />
      <van-field v-model="form.password" type="password" label="密码" placeholder="6-20位" :rules="[{ required: true, message: '请输入密码' }]" />
      <van-field v-model="form.confirmPassword" type="password" label="确认密码" placeholder="再次输入密码" :rules="[{ required: true, message: '请确认密码' }]" />
      <div style="margin:24px 16px 12px">
        <van-button round block type="primary" native-type="submit" :loading="loading">注册</van-button>
      </div>
      <div style="text-align:center"><router-link to="/login" style="color:#1989fa">已有账号？去登录</router-link></div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../api'

const router = useRouter()
const form = reactive({ studentNo: '', email: '', nickname: '', password: '', confirmPassword: '' })
const loading = ref(false)

const submit = async () => {
  loading.value = true
  try {
    await request.post('/user/auth/register', form)
    showToast('注册成功，请登录')
    router.replace('/login')
  } finally {
    loading.value = false
  }
}
</script>
