<template>
  <div>
    <van-nav-bar title="我的收藏" left-arrow @click-left="$router.back()" />
    <van-empty v-if="!list.length" description="还没有收藏" />
    <van-cell v-for="item in list" :key="item.id" @click="$router.push(`/item/${item.id}`)">
      <template #title>{{ item.title }}</template>
      <template #label>RM {{ item.price }} · {{ item.sellerNickname }}</template>
      <template #right-icon>
        <van-button size="mini" plain @click.stop="remove(item)">取消</van-button>
      </template>
    </van-cell>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast } from 'vant'
import request from '../api'

const list = ref([])

const load = async () => {
  const data = await request.get('/user/favorite/page', { params: { page: 1, pageSize: 50 } })
  list.value = data.records
}

const remove = async (item) => {
  await request.delete(`/user/favorite/${item.id}`)
  list.value = list.value.filter((i) => i.id !== item.id)
  showToast('已取消')
}

onMounted(load)
</script>
