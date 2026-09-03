<template>
  <div>
    <van-nav-bar title="我的发布" left-arrow @click-left="$router.back()" />
    <van-empty v-if="!list.length" description="还没有发布商品" />
    <van-card v-for="item in list" :key="item.id" :title="item.title" :thumb="item.cover || placeholder"
              @click-thumb="$router.push(`/item/${item.id}`)">
      <template #desc>
        {{ item.wantCount }}人想要 · {{ item.viewCount }}次浏览
        <span v-if="item.auditRemark" style="color:#ee0a24"> · {{ item.auditRemark }}</span>
      </template>
      <template #price>RM {{ item.price }}</template>
      <template #tags>
        <van-tag :type="['info','success','warning','primary','danger'][item.status]">
          {{ ['已下架','在售','交易中','已售出','待审核'][item.status] }}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="small" @click="$router.push(`/publish?id=${item.id}`)">编辑</van-button>
        <van-button v-if="item.status === 1 || item.status === 2" size="small" type="success" @click="sold(item)">标记已售</van-button>
        <van-button v-if="item.status === 0" size="small" type="warning" @click="relist(item)">重新上架</van-button>
        <van-button size="small" type="danger" plain @click="remove(item)">删除</van-button>
      </template>
    </van-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showSuccessToast, showConfirmDialog } from 'vant'
import request from '../api'

const placeholder = 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'
const list = ref([])

const load = async () => {
  const data = await request.get('/user/item/mine', { params: { page: 1, pageSize: 50 } })
  list.value = data.records
}

const sold = async (item) => {
  await request.put(`/user/item/sold/${item.id}`)
  showSuccessToast('已标记售出')
  load()
}

const relist = async (item) => {
  await request.put(`/user/item/relist/${item.id}`)
  showSuccessToast('已重新上架')
  load()
}

const remove = async (item) => {
  await showConfirmDialog({ title: '确认删除该商品？' })
  await request.delete(`/user/item/${item.id}`)
  load()
}

onMounted(load)
</script>
