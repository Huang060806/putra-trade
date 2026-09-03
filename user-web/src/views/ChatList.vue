<template>
  <div>
    <van-nav-bar title="我的私聊" left-arrow @click-left="$router.back()" />
    <van-empty v-if="!list.length" description="还没有私聊，去商品页找卖家聊聊吧" />
    <van-cell v-for="s in list" :key="s.peerId" @click="$router.push(`/chat/${s.peerId}`)">
      <template #title>
        <van-badge :content="s.unreadCount || ''" :show-zero="false">{{ s.peerNickname }}</van-badge>
      </template>
      <template #label>{{ s.lastMessage }}</template>
      <template #value>{{ s.lastTime }}</template>
    </van-cell>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api'

const list = ref([])
onMounted(async () => {
  list.value = await request.get('/user/chat/sessions')
})
</script>
