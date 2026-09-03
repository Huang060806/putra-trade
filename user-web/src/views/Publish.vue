<template>
  <div style="padding-bottom:70px">
    <van-nav-bar :title="isEdit ? '编辑商品' : '发布商品'" left-arrow @click-left="$router.back()" />
    <van-form @submit="submit">
      <van-field v-model="form.title" label="标题" placeholder="一句话描述你的闲置" :rules="[{ required: true, message: '请输入标题' }]" />
      <van-field label="分类">
        <template #input>
          <van-radio-group v-model="form.categoryId" direction="horizontal">
            <van-radio v-for="c in categories" :key="c.id" :name="c.id" style="margin:4px 8px 4px 0">{{ c.name }}</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field v-model.number="form.price" type="number" label="售价(RM)" :rules="[{ required: true, message: '请输入价格' }]" />
      <van-field v-model.number="form.originalPrice" type="number" label="原价(RM)" placeholder="选填" />
      <van-field label="成色">
        <template #input>
          <van-radio-group v-model="form.conditionLevel" direction="horizontal">
            <van-radio v-for="(t, v) in conditions" :key="v" :name="Number(v)" style="margin:4px 8px 4px 0">{{ t }}</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field label="交货地点">
        <template #input>
          <van-radio-group v-model="form.campusArea" direction="horizontal">
            <van-radio v-for="a in areas" :key="a" :name="a" style="margin:4px 8px 4px 0">{{ a }}</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field v-model="form.description" type="textarea" rows="4" label="描述" placeholder="成色细节、入手渠道、面交时间等" />
      <van-field label="封面图">
        <template #input><van-uploader :max-count="1" :after-read="(f) => upload(f, 'cover')" /></template>
      </van-field>
      <van-field label="补充图">
        <template #input><van-uploader :max-count="4" :after-read="(f) => upload(f, 'images')" /></template>
      </van-field>
      <div v-if="form.cover" style="padding:8px 16px; color:#07c160; font-size:13px">封面已上传 ✓ 补充图 {{ form.images.length }} 张</div>
      <div style="margin:24px 16px">
        <van-button round block type="primary" native-type="submit" :loading="loading">{{ isEdit ? '保存修改' : '立即发布' }}</van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast, showSuccessToast } from 'vant'
import request from '../api'

const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const loading = ref(false)
const categories = ref([])
const conditions = { 1: '全新未拆', 2: '几乎全新', 3: '轻微痕迹', 4: '明显痕迹' }
const areas = ['KMR', 'College 10', 'DKP', 'Library', '其他']
const form = reactive({ id: null, title: '', categoryId: null, price: null, originalPrice: null, conditionLevel: 3, campusArea: 'KMR', description: '', cover: '', images: [] })

const upload = async (file, target) => {
  const fd = new FormData()
  fd.append('file', file.file)
  const url = await request.post('/user/common/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
  if (target === 'cover') form.cover = url
  else form.images.push(url)
  showSuccessToast('上传成功')
}

const submit = async () => {
  if (!form.categoryId) { showToast('请选择分类'); return }
  if (!form.cover) { showToast('请上传封面图'); return }
  loading.value = true
  try {
    if (isEdit.value) await request.put('/user/item', form)
    else await request.post('/user/item', form)
    showSuccessToast(isEdit.value ? '已保存' : '发布成功')
    router.replace('/mine/items')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  categories.value = await request.get('/user/category/list')
  if (route.query.id) {
    isEdit.value = true
    const item = await request.get(`/user/item/${route.query.id}`)
    Object.assign(form, {
      id: item.id, title: item.title, categoryId: item.categoryId, price: item.price,
      originalPrice: item.originalPrice, conditionLevel: item.conditionLevel,
      campusArea: item.campusArea || 'KMR', description: item.description,
      cover: item.cover || '', images: item.images || []
    })
  }
})
</script>
