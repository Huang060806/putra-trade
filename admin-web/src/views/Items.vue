<template>
  <div>
    <el-form inline>
      <el-form-item label="关键字"><el-input v-model="query.keyword" clearable placeholder="标题/描述" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" style="width:140px">
          <el-option v-for="(l, v) in statusMap" :key="v" :label="l" :value="Number(v)" />
        </el-select>
      </el-form-item>
      <el-button type="primary" @click="load(1)">查询</el-button>
    </el-form>

    <el-table :data="list" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="categoryName" label="分类" width="110" />
      <el-table-column prop="sellerNickname" label="卖家" width="100" />
      <el-table-column prop="price" label="价格(RM)" width="100" />
      <el-table-column prop="wantCount" label="想要" width="70" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditRemark" label="审核备注" min-width="140" show-overflow-tooltip />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 4">
            <el-button size="small" type="success" @click="audit(row, true)">通过</el-button>
            <el-button size="small" type="danger" @click="audit(row, false)">驳回</el-button>
          </template>
          <el-button v-if="row.status !== 0" size="small" type="warning" @click="takedown(row)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total"
      :page-size="query.pageSize" :current-page="query.page" @current-change="load" />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api'

const statusMap = { 0: '已下架', 1: '在售', 2: '交易中', 3: '已售出', 4: '待审核' }
const statusType = { 0: 'info', 1: 'success', 2: 'warning', 3: '', 4: 'danger' }

const query = reactive({ page: 1, pageSize: 10, keyword: '', status: null })
const list = ref([])
const total = ref(0)
const loading = ref(false)

const load = async (page = query.page) => {
  query.page = page
  loading.value = true
  try {
    const data = await request.get('/admin/item/page', { params: query })
    list.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const audit = async (row, pass) => {
  let remark = '审核通过'
  if (!pass) {
    remark = await ElMessageBox.prompt('请输入驳回原因', '驳回商品', { inputValue: row.auditRemark || '内容违规' })
      .then((r) => r.value).catch(() => null)
    if (remark === null) return
  }
  await request.put(`/admin/item/audit/${row.id}`, null, { params: { pass, remark } })
  ElMessage.success('已处理')
  load()
}

const takedown = async (row) => {
  const reason = await ElMessageBox.prompt('请输入下架理由（将通知卖家）', '违规下架').then((r) => r.value).catch(() => null)
  if (!reason) return
  await request.put(`/admin/item/takedown/${row.id}`, null, { params: { reason } })
  ElMessage.success('已下架并通知卖家')
  load()
}

onMounted(() => load(1))
</script>
