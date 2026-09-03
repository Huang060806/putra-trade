<template>
  <div>
    <el-form inline>
      <el-form-item label="对象">
        <el-select v-model="targetType" clearable placeholder="全部" style="width:120px">
          <el-option label="商品" :value="1" /><el-option label="用户" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="status" clearable placeholder="全部" style="width:120px">
          <el-option label="待处理" :value="0" /><el-option label="已处理" :value="1" /><el-option label="已驳回" :value="2" />
        </el-select>
      </el-form-item>
      <el-button type="primary" @click="load(1)">查询</el-button>
    </el-form>

    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="对象" width="90">
        <template #default="{ row }">
          <el-tag :type="row.targetType === 1 ? 'warning' : 'danger'">{{ row.targetType === 1 ? '商品' : '用户' }} #{{ row.targetId }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reporterId" label="举报人" width="90" />
      <el-table-column prop="reason" label="举报原因" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="['danger', 'success', 'info'][row.status]">{{ ['待处理', '已处理', '已驳回'][row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="handleResult" label="处理结果" min-width="160" show-overflow-tooltip />
      <el-table-column prop="createTime" label="举报时间" width="160" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" size="small" type="primary" @click="openHandle(row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" :page-size="10" @current-change="load" />

    <el-dialog v-model="dialogVisible" title="处理举报" width="420px">
      <el-form label-width="80px">
        <el-form-item label="处理动作">
          <el-select v-model="handleForm.action" style="width:100%">
            <el-option v-if="current?.targetType === 1" label="下架该商品" value="TAKEDOWN" />
            <template v-if="current?.targetType === 2">
              <el-option label="禁止发布" value="BAN_PUBLISH" />
              <el-option label="禁止私聊" value="BAN_CHAT" />
              <el-option label="封禁账号" value="BAN_ACCOUNT" />
            </template>
            <el-option label="驳回举报" value="DISMISS" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明">
          <el-input v-model="handleForm.handleResult" type="textarea" :rows="3" placeholder="必填，将通知相关用户" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!handleForm.action || !handleForm.handleResult" @click="submitHandle">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../api'

const targetType = ref(null)
const status = ref(null)
const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const current = ref(null)
const handleForm = reactive({ action: '', handleResult: '' })

const load = async (page = 1) => {
  const data = await request.get('/admin/report/page', { params: { targetType: targetType.value, status: status.value, page, pageSize: 10 } })
  list.value = data.records
  total.value = data.total
}

const openHandle = (row) => {
  current.value = row
  handleForm.action = ''
  handleForm.handleResult = ''
  dialogVisible.value = true
}

const submitHandle = async () => {
  await request.put('/admin/report/handle', { reportId: current.value.id, ...handleForm })
  ElMessage.success('处理完成')
  dialogVisible.value = false
  load()
}

onMounted(() => load(1))
</script>
