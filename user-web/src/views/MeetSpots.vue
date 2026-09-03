<template>
  <div>
    <van-nav-bar title="面交地点" left-arrow @click-left="$router.back()" />
    <van-cell v-for="s in list" :key="s.id">
      <template #title>
        {{ s.spotName }}
        <van-tag v-if="s.isDefault" type="primary" style="margin-left:4px">默认</van-tag>
      </template>
      <template #label>{{ s.contactName }} {{ s.contactPhone }}<span v-if="s.remark"> · {{ s.remark }}</span></template>
      <template #right-icon>
        <van-button size="mini" type="danger" plain @click="remove(s)">删除</van-button>
      </template>
    </van-cell>
    <van-cell-group inset style="margin-top:16px">
      <van-form @submit="add">
        <van-field v-model="form.spotName" label="地点" placeholder="如 KMR 宿管门口" :rules="[{ required: true, message: '必填' }]" />
        <van-field v-model="form.contactName" label="联系人" :rules="[{ required: true, message: '必填' }]" />
        <van-field v-model="form.contactPhone" label="电话" :rules="[{ required: true, message: '必填' }]" />
        <van-field v-model="form.remark" label="备注" placeholder="选填：方便的时间段" />
        <van-field label="设为默认"><template #input><van-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" size="20" /></template></van-field>
        <div style="margin:16px"><van-button round block type="primary" native-type="submit">添加地点</van-button></div>
      </van-form>
    </van-cell-group>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { showSuccessToast } from 'vant'
import request from '../api'

const list = ref([])
const form = reactive({ spotName: '', contactName: '', contactPhone: '', remark: '', isDefault: 0 })

const load = async () => { list.value = await request.get('/user/meetSpot/list') }

const add = async () => {
  await request.post('/user/meetSpot', form)
  showSuccessToast('已添加')
  Object.assign(form, { spotName: '', contactName: '', contactPhone: '', remark: '', isDefault: 0 })
  load()
}

const remove = async (s) => {
  await request.delete(`/user/meetSpot/${s.id}`)
  load()
}

onMounted(load)
</script>
