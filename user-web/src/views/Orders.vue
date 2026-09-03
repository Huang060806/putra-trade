<template>
  <div style="padding-bottom:60px">
    <van-nav-bar title="我的订单" left-arrow @click-left="$router.back()" />
    <van-empty v-if="!list.length" description="暂无订单" />
    <van-card v-for="o in list" :key="o.id" :title="o.itemTitle" :thumb="o.itemCover || placeholder">
      <template #desc>
        <div>面交：{{ o.meetSpotInfo }}</div>
        <div>单号：{{ o.orderNo }}</div>
      </template>
      <template #price>RM {{ o.price }}</template>
      <template #tags>
        <van-tag :type="['warning','primary','success','info'][o.status]">
          {{ ['待支付','待面交','已完成','已取消'][o.status] }}
        </van-tag>
        <van-tag v-if="o.sellerId === myId" plain style="margin-left:4px">我卖出的</van-tag>
      </template>
      <template #footer>
        <template v-if="o.buyerId === myId">
          <van-button v-if="o.status === 0" size="small" type="primary" @click="pay(o)">模拟支付</van-button>
          <van-button v-if="o.status === 1" size="small" type="success" @click="confirm(o)">确认面交完成</van-button>
        </template>
        <van-button v-if="o.status === 0 || o.status === 1" size="small" @click="cancel(o)">取消订单</van-button>
      </template>
    </van-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showSuccessToast, showConfirmDialog } from 'vant'
import request from '../api'
import { getUser } from '../utils/auth'

const placeholder = 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'
const list = ref([])
const myId = getUser()?.id

const load = async () => {
  const data = await request.get('/user/order/page', { params: { page: 1, pageSize: 50 } })
  list.value = data.records
}

const pay = async (o) => {
  await request.put(`/user/order/pay/${o.id}`)
  showSuccessToast('支付成功')
  load()
}

const confirm = async (o) => {
  await showConfirmDialog({ title: '确认已完成面交并拿到商品？' })
  await request.put(`/user/order/confirm/${o.id}`)
  showSuccessToast('交易完成')
  load()
}

const cancel = async (o) => {
  await showConfirmDialog({ title: '确认取消该订单？商品将恢复在售' })
  await request.put(`/user/order/cancel/${o.id}`, null, { params: { reason: '用户主动取消' } })
  showSuccessToast('已取消')
  load()
}

onMounted(load)
</script>
