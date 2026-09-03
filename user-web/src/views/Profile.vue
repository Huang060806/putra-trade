<template>
  <div>
    <van-nav-bar title="个人资料" left-arrow @click-left="$router.back()" />
    <van-form @submit="submit">
      <van-field v-model="form.nickname" label="昵称" />
      <van-field v-model="form.phone" label="手机号" />
      <van-field v-model="form.wechat" label="微信号" placeholder="买家点想要后可见" />
      <van-field v-model="form.whatsapp" label="WhatsApp" />
      <van-field v-model="form.dormArea" label="宿舍区域" placeholder="如 KMR / College 10 / DKP" />
      <div style="margin:24px 16px">
        <van-button round block type="primary" native-type="submit">保存</van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { showSuccessToast } from 'vant'
import request from '../api'

const router = useRouter()
const form = reactive({ nickname: '', phone: '', wechat: '', whatsapp: '', dormArea: '' })

const submit = async () => {
  await request.put('/user/auth/profile', form)
  showSuccessToast('已保存')
  router.back()
}
</script>
