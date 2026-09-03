<template>
  <div>
    <van-nav-bar title="我想要的" />
    <van-empty v-if="!list.length" description="还没有想要的商品" />
    <van-cell v-for="item in list" :key="item.id" @click="$router.push(`/item/${item.id}`)">
      <template #title>{{ item.title }}</template>
      <template #label>
        RM {{ item.price }} · {{ item.sellerNickname }} ·
        <van-tag :type="item.status === 1 ? 'success' : 'info'">
          {{ ['已下架','在售','交易中','已售出','审核中'][item.status] }}
        </van-tag>
      </template>
      <template #right-icon>
        <van-button size="mini" plain @click.stop="remove(item)">取消</van-button>
      </template>
    </van-cell>
    <tabbar active="want" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast } from 'vant'
import request from '../api'
import Tabbar from './Tabbar.vue'

const list = ref([])

const load = async () => {
  const data = await request.get('/user/want/page', { params: { page: 1, pageSize: 50 } })
  list.value = data.records
}

const remove = async (item) => {
  await request.delete(`/user/want/${item.id}`)
  list.value = list.value.filter((i) => i.id !== item.id)
  showToast('已取消')
}

onMounted(load)
</script>
