<template>
  <div>
    <el-form inline>
      <el-form-item label="关键字"><el-input v-model="keyword" clearable placeholder="学号/昵称" /></el-form-item>
      <el-button type="primary" @click="load(1)">查询</el-button>
    </el-form>
    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="studentNo" label="学号" width="130" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
      <el-table-column prop="dormArea" label="宿舍区" width="110" />
      <el-table-column label="状态" width="220">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="danger">已封号</el-tag>
          <el-tag v-else type="success">正常</el-tag>
          <el-tag v-if="row.banPublish === 1" type="warning" style="margin-left:6px">禁发布</el-tag>
          <el-tag v-if="row.banChat === 1" type="warning" style="margin-left:6px">禁私聊</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="160" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openBan(row)">管理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" :page-size="10" @current-change="load" />

    <el-dialog v-model="visible" :title="`管理用户：${current?.nickname}`" width="420px">
      <el-form label-width="100px">
        <el-form-item label="禁止发布"><el-switch v-model="banForm.banPublish" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="禁止私聊"><el-switch v-model="banForm.banChat" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="账号状态">
          <el-switch v-model="banForm.status" :active-value="1" :inactive-value="0" active-text="正常" inactive-text="封号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submitBan">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api'

const keyword = ref('')
const list = ref([])
const total = ref(0)
const visible = ref(false)
const current = ref(null)
const banForm = reactive({ banPublish: 0, banChat: 0, status: 1 })

const load = async (page = 1) => {
  const data = await request.get('/admin/member/page', { params: { keyword: keyword.value, page, pageSize: 10 } })
  list.value = data.records
  total.value = data.total
}

const openBan = (row) => {
  current.value = row
  banForm.banPublish = row.banPublish
  banForm.banChat = row.banChat
  banForm.status = row.status
  visible.value = true
}

const submitBan = async () => {
  await request.put('/admin/member/ban', { memberId: current.value.id, ...banForm })
  ElMessage.success('已保存')
  visible.value = false
  load()
}

onMounted(() => load(1))
</script>
