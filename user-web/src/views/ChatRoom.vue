<template>
  <div style="display:flex; flex-direction:column; height:100vh">
    <van-nav-bar :title="peerName" left-arrow @click-left="$router.back()" />
    <div ref="msgBox" style="flex:1; overflow-y:auto; padding:12px; background:#f7f8fa">
      <div v-for="m in messages" :key="m.id" :style="{ textAlign: m.senderId === myId ? 'right' : 'left', marginBottom: '10px' }">
        <span :style="{
          display:'inline-block', maxWidth:'70%', padding:'8px 12px', borderRadius:'10px',
          background: m.senderId === myId ? '#07c160' : '#fff',
          color: m.senderId === myId ? '#fff' : '#333', textAlign:'left'
        }">{{ m.content }}</span>
        <div style="font-size:11px; color:#999; margin-top:2px">{{ m.createTime }}</div>
      </div>
    </div>
    <div style="display:flex; gap:8px; padding:8px; background:#fff; border-top:1px solid #eee">
      <van-field v-model="input" placeholder="输入消息..." style="flex:1" @keyup.enter="send" />
      <van-button type="primary" :disabled="!input.trim()" @click="send">发送</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import request from '../api'
import { getUser } from '../utils/auth'

const route = useRoute()
const peerId = Number(route.params.peerId)
const myId = getUser()?.id
const messages = ref([])
const input = ref('')
const peerName = ref('私聊')
const msgBox = ref()
let ws = null
let pollTimer = null

const scrollBottom = async () => {
  await nextTick()
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
}

const load = async () => {
  const data = await request.get('/user/chat/history', { params: { peerId, page: 1, pageSize: 50 } })
  messages.value = data.records
  scrollBottom()
}

const send = async () => {
  const content = input.value.trim()
  if (!content) return
  const msg = await request.post('/user/chat/send', {
    receiverId: peerId,
    itemId: route.query.itemId ? Number(route.query.itemId) : undefined,
    content
  })
  messages.value.push(msg)
  input.value = ''
  scrollBottom()
}

onMounted(async () => {
  await load()
  // 从会话列表拿对方昵称（列表接口已联 member）
  try {
    const sessions = await request.get('/user/chat/sessions')
    peerName.value = sessions.find((s) => s.peerId === peerId)?.peerNickname || '私聊'
  } catch (e) {}

  // WebSocket 实时接收；失败则 5s 轮询兜底
  try {
    const proto = location.protocol === 'https:' ? 'wss' : 'ws'
    ws = new WebSocket(`${proto}://${location.host}/ws/${myId}`)
    ws.onmessage = (e) => {
      try {
        const payload = JSON.parse(e.data)
        if (payload.type === 'chat' && payload.data.senderId === peerId) {
          messages.value.push(payload.data)
          scrollBottom()
        }
      } catch (err) {}
    }
    ws.onerror = () => { pollTimer = setInterval(load, 5000) }
  } catch (e) {
    pollTimer = setInterval(load, 5000)
  }
})

onUnmounted(() => {
  ws?.close()
  if (pollTimer) clearInterval(pollTimer)
})
</script>
