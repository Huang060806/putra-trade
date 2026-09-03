<template>
  <div>
    <van-sticky>
      <van-search v-model="query.keyword" placeholder="搜索商品" @search="reload" />
      <van-dropdown-menu>
        <van-dropdown-item v-model="query.categoryId" :options="categoryOptions" @change="reload" />
        <van-dropdown-item v-model="query.campusArea" :options="areaOptions" @change="reload" />
        <van-dropdown-item v-model="query.sortBy" :options="sortOptions" @change="reload" />
      </van-dropdown-menu>
    </van-sticky>

    <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="loadMore">
      <div style="display:grid; grid-template-columns:1fr 1fr; gap:8px; padding:8px">
        <div v-for="item in list" :key="item.id" @click="$router.push(`/item/${item.id}`)"
             style="background:#fff; border-radius:8px; overflow:hidden">
          <van-image :src="item.cover || placeholder" fit="cover" style="width:100%; height:140px" />
          <div style="padding:8px">
            <div style="font-size:14px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap">{{ item.title }}</div>
            <div style="color:#ee0a24; font-weight:bold; margin-top:4px">RM {{ item.price }}</div>
            <div style="color:#999; font-size:12px; margin-top:4px">
              {{ item.sellerNickname }} · {{ item.campusArea }} · {{ item.wantCount }}人想要
            </div>
          </div>
        </div>
      </div>
    </van-list>

    <van-tabbar route>
      <van-tabbar-item icon="home-o" to="/">首页</van-tabbar-item>
      <van-tabbar-item icon="plus" to="/publish">发布</van-tabbar-item>
      <van-tabbar-item icon="star-o" to="/want">想要</van-tabbar-item>
      <van-tabbar-item icon="chat-o" to="/messages" :badge="unread || ''">消息</van-tabbar-item>
      <van-tabbar-item icon="user-o" to="/mine">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import request from '../api'
import { getToken } from '../utils/auth'

const placeholder = 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'
const query = reactive({ keyword: '', categoryId: 0, campusArea: '', sortBy: 'latest', page: 1, pageSize: 10 })
const list = ref([])
const loading = ref(false)
const finished = ref(false)
const categories = ref([])
const unread = ref(0)

const categoryOptions = computed(() => [{ text: '全部分类', value: 0 }, ...categories.value.map((c) => ({ text: c.name, value: c.id }))])
const areaOptions = [
  { text: '全部地点', value: '' }, { text: 'KMR', value: 'KMR' }, { text: 'College 10', value: 'College 10' },
  { text: 'DKP', value: 'DKP' }, { text: 'Library', value: 'Library' }
]
const sortOptions = [
  { text: '最新发布', value: 'latest' }, { text: '价格从低到高', value: 'price_asc' },
  { text: '价格从高到低', value: 'price_desc' }, { text: '热度优先', value: 'hot' }
]

const loadMore = async () => {
  try {
    const params = { ...query, categoryId: query.categoryId || undefined, campusArea: query.campusArea || undefined }
    const data = await request.get('/user/item/page', { params })
    list.value.push(...data.records)
    query.page++
    if (list.value.length >= data.total) finished.value = true
  } finally {
    loading.value = false
  }
}

const reload = () => {
  query.page = 1
  list.value = []
  finished.value = false
  loading.value = true
  loadMore()
}

onMounted(async () => {
  categories.value = await request.get('/user/category/list')
  if (getToken()) {
    unread.value = await request.get('/user/message/unread').catch(() => 0)
  }
})
</script>
