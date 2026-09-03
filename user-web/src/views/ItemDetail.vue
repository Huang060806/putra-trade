<template>
  <div v-if="item" style="padding-bottom:60px">
    <van-nav-bar title="商品详情" left-arrow @click-left="$router.back()" />
    <van-swipe :autoplay="3000" style="height:300px" v-if="allImages.length">
      <van-swipe-item v-for="(img, i) in allImages" :key="i">
        <van-image :src="img" fit="cover" style="width:100%; height:300px" />
      </van-swipe-item>
    </van-swipe>
    <van-cell-group inset style="margin-top:8px">
      <van-cell>
        <template #title>
          <span style="color:#ee0a24; font-size:22px; font-weight:bold">RM {{ item.price }}</span>
          <span v-if="item.originalPrice" style="color:#999; text-decoration:line-through; margin-left:8px">RM {{ item.originalPrice }}</span>
        </template>
      </van-cell>
      <van-cell :title="item.title" :label="`成色：${conditionText} · ${item.campusArea} · ${item.wantCount}人想要 · ${item.viewCount}次浏览`" />
      <van-cell title="描述" :label="item.description || '暂无描述'" />
      <van-cell title="卖家">
        <template #default>{{ item.sellerNickname }}</template>
      </van-cell>
      <van-cell v-if="item.status !== 1" title="状态">
        <template #default><van-tag type="warning">{{ ['已下架','在售','交易中','已售出','审核中'][item.status] }}</van-tag></template>
      </van-cell>
    </van-cell-group>

    <div style="margin:12px 16px">
      <van-button size="small" plain type="danger" @click="reportVisible = true">举报该商品</van-button>
    </div>

    <van-submit-bar :price="0" button-text="" :loading="false">
      <div style="display:flex; gap:8px; width:100%; padding:0 8px">
        <van-button icon="star" :type="item.favorited ? 'warning' : 'default'" size="small" @click="toggleFavorite">
          {{ item.favorited ? '已收藏' : '收藏' }}
        </van-button>
        <van-button type="warning" size="small" style="flex:1" @click="want" :disabled="item.status !== 1">
          {{ item.wanted ? '已想要' : '我想要' }}
        </van-button>
        <van-button type="primary" size="small" style="flex:1" @click="buy" :disabled="item.status !== 1">立即下单</van-button>
      </div>
    </van-submit-bar>

    <van-dialog v-model:show="contactVisible" title="卖家联系方式" confirm-button-text="知道了">
      <div style="padding:16px; line-height:2">
        <div>卖家：{{ contact.sellerNickname }}</div>
        <div v-if="contact.wechat">微信：<b>{{ contact.wechat }}</b></div>
        <div v-if="contact.whatsapp">WhatsApp：<b>{{ contact.whatsapp }}</b></div>
        <div v-if="contact.phone">电话：<b>{{ contact.phone }}</b></div>
        <div v-if="!contact.wechat && !contact.whatsapp && !contact.phone">卖家暂未填写联系方式</div>
      </div>
    </van-dialog>

    <van-dialog v-model:show="reportVisible" title="举报商品" show-cancel-button @confirm="submitReport">
      <van-field v-model="reportReason" type="textarea" rows="3" placeholder="请填写举报原因" />
    </van-dialog>

    <van-popup v-model:show="buyVisible" position="bottom" round>
      <div style="padding:16px">
        <h3>选择面交地点</h3>
        <van-radio-group v-model="selectedSpot">
          <van-cell v-for="s in spots" :key="s.id" clickable @click="selectedSpot = s.id">
            <template #title>{{ s.spotName }}（{{ s.contactName }} {{ s.contactPhone }}）</template>
            <template #right-icon><van-radio :name="s.id" /></template>
          </van-cell>
        </van-radio-group>
        <van-button v-if="!spots.length" plain block to="/mine/spots">先去添加面交地点</van-button>
        <van-button v-else type="primary" block style="margin-top:12px" :disabled="!selectedSpot" @click="submitOrder">确认下单</van-button>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast, showSuccessToast } from 'vant'
import request from '../api'
import { getToken } from '../utils/auth'

const route = useRoute()
const router = useRouter()
const item = ref(null)
const contactVisible = ref(false)
const contact = ref({})
const reportVisible = ref(false)
const reportReason = ref('')
const buyVisible = ref(false)
const spots = ref([])
const selectedSpot = ref(null)

const conditionText = computed(() => ['', '全新未拆', '几乎全新', '轻微使用痕迹', '明显使用痕迹'][item.value?.conditionLevel] || '未知')
const allImages = computed(() => [item.value?.cover, ...(item.value?.images || [])].filter(Boolean))

const needLogin = () => {
  if (!getToken()) { router.push('/login'); return true }
  return false
}

const want = async () => {
  if (needLogin()) return
  if (item.value.wanted) {
    await request.delete(`/user/want/${item.value.id}`)
    item.value.wanted = false
    item.value.wantCount--
    showToast('已取消想要')
  } else {
    contact.value = await request.post(`/user/want/${item.value.id}`)
    item.value.wanted = true
    item.value.wantCount++
    contactVisible.value = true
  }
}

const toggleFavorite = async () => {
  if (needLogin()) return
  if (item.value.favorited) {
    await request.delete(`/user/favorite/${item.value.id}`)
    item.value.favorited = false
    showToast('已取消收藏')
  } else {
    await request.post(`/user/favorite/${item.value.id}`)
    item.value.favorited = true
    showSuccessToast('已收藏')
  }
}

const buy = async () => {
  if (needLogin()) return
  spots.value = await request.get('/user/meetSpot/list')
  buyVisible.value = true
}

const submitOrder = async () => {
  const order = await request.post('/user/order/submit', { itemId: item.value.id, meetSpotId: selectedSpot.value })
  buyVisible.value = false
  showSuccessToast('下单成功，请尽快支付')
  router.push('/orders')
}

const submitReport = async () => {
  if (needLogin()) return
  await request.post('/user/report', { targetType: 1, targetId: item.value.id, reason: reportReason.value })
  showSuccessToast('举报已提交，等待平台审核')
}

onMounted(async () => {
  item.value = await request.get(`/user/item/${route.params.id}`)
})
</script>
