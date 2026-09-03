<template>
  <div>
    <el-form inline>
      <el-form-item label="敏感词"><el-input v-model="word" placeholder="输入新敏感词" /></el-form-item>
      <el-form-item label="类别">
        <el-select v-model="type" style="width:140px">
          <el-option label="政治" :value="1" /><el-option label="色情" :value="2" /><el-option label="违禁品" :value="3" />
        </el-select>
      </el-form-item>
      <el-button type="primary" @click="add">添加</el-button>
    </el-form>
    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="word" label="敏感词" />
      <el-table-column label="类别" width="120">
        <template #default="{ row }">{{ ['', '政治', '色情', '违禁品'][row.type] }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="添加时间" width="180" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api'

const list = ref([])
const word = ref('')
const type = ref(3)

const load = async () => { list.value = await request.get('/admin/sensitiveWord/list') }

const add = async () => {
  if (!word.value.trim()) return
  await request.post('/admin/sensitiveWord', null, { params: { word: word.value.trim(), type: type.value } })
  ElMessage.success('已添加并即时生效')
  word.value = ''
  load()
}

const remove = async (row) => {
  await request.delete(`/admin/sensitiveWord/${row.id}`)
  load()
}

onMounted(load)
</script>
