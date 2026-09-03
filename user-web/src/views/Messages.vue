<template>
  <div>
    <van-nav-bar title="消息">
      <template #right><van-button size="mini" plain @click="readAll">全部已读</van-button></template>
    </van-nav-bar>
    <van-empty v-if="!list.length" description="暂无消息" />
    <van-cell v-for="m in list" :key="m.id">
      <template #title>
        <van-tag :type="['','primary','warning','danger','success'][m.type]" style="margin-right:4px">
          {{ ['','订单','订单变更','平台','想要'][m.type] }}
        </van-tag>
        <van-badge v-if="!m.isRead" dot>{{ m.content }}</van-badge>
        <span v-else>{{ m.content }}</span>
      </template>
      <template #label>{{ m.createTime }}</template>
    </van-cell>
    <tabbar active="messages" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api'
import Tabbar from './Tabbar.vue'

const list = ref([])

const load = async () => {
  const data = await request.get('/user/message/page', { params: { page: 1, pageSize: 50 } })
  list.value = data.records
}

const readAll = async () => {
  await request.put('/user/message/readAll')
  load()
}

onMounted(load)
</script>
