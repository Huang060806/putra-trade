<template>
  <div>
    <van-cell-group inset style="margin-top:16px">
      <van-cell title="昵称" :value="user?.nickname" />
      <van-cell title="学号" :value="user?.studentNo" />
    </van-cell-group>
    <van-cell-group inset style="margin-top:12px">
      <van-cell title="我的发布" is-link to="/mine/items" icon="shop-o" />
      <van-cell title="我想要的" is-link to="/want" icon="star-o" />
      <van-cell title="我的收藏" is-link to="/favorite" icon="like-o" />
      <van-cell title="我的订单" is-link to="/orders" icon="orders-o" />
      <van-cell title="面交地点管理" is-link to="/mine/spots" icon="location-o" />
      <van-cell title="个人资料设置" is-link to="/mine/profile" icon="setting-o" />
    </van-cell-group>
    <div style="margin:24px 16px">
      <van-button round block type="danger" plain @click="logout">退出登录</van-button>
    </div>
    <tabbar active="mine" />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { getUser, clearAuth } from '../utils/auth'
import Tabbar from './Tabbar.vue'

const router = useRouter()
const user = getUser()

const logout = () => {
  clearAuth()
  router.replace('/login')
}
</script>
