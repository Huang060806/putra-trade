<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="4" v-for="c in cards" :key="c.label">
        <el-card><div style="color:#999">{{ c.label }}</div><div style="font-size:28px; font-weight:bold">{{ c.value }}</div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="14"><el-card><div ref="trendRef" style="height:320px" /></el-card></el-col>
      <el-col :span="10"><el-card><div ref="pieRef" style="height:320px" /></el-card></el-col>
    </el-row>
    <el-card style="margin-top:16px">
      <template #header>想要榜 Top10</template>
      <el-table :data="data.topItems || []">
        <el-table-column prop="title" label="商品" />
        <el-table-column prop="wantCount" label="想要人数" width="120" />
        <el-table-column prop="viewCount" label="浏览量" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '../api'

const data = ref({})
const trendRef = ref()
const pieRef = ref()

const cards = computed(() => [
  { label: '注册用户', value: data.value.totalMembers ?? '-' },
  { label: '商品总数', value: data.value.totalItems ?? '-' },
  { label: '成交订单', value: data.value.completedOrders ?? '-' },
  { label: '待处理举报', value: data.value.pendingReports ?? '-' },
  { label: '待审核商品', value: data.value.pendingAudit ?? '-' }
])

const fillDates = (list) => Object.fromEntries((list || []).map((i) => [i.date, i.count]))

onMounted(async () => {
  data.value = await request.get('/admin/statistic/dashboard')

  const days = [...Array(30)].map((_, i) => {
    const d = new Date(Date.now() - (29 - i) * 86400000)
    return d.toISOString().slice(0, 10)
  })
  const members = fillDates(data.value.memberTrend)
  const items = fillDates(data.value.itemTrend)
  const reports = fillDates(data.value.reportHandleTrend)

  echarts.init(trendRef.value).setOption({
    title: { text: '近30天趋势' },
    tooltip: { trigger: 'axis' },
    legend: { data: ['新增用户', '新增商品', '违规处理'] },
    xAxis: { type: 'category', data: days },
    yAxis: { type: 'value' },
    series: [
      { name: '新增用户', type: 'line', data: days.map((d) => members[d] || 0) },
      { name: '新增商品', type: 'line', data: days.map((d) => items[d] || 0) },
      { name: '违规处理', type: 'line', data: days.map((d) => reports[d] || 0) }
    ]
  })

  echarts.init(pieRef.value).setOption({
    title: { text: '分类商品占比' },
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '60%', data: (data.value.categoryDistribution || []).map((c) => ({ name: c.name, value: c.count })) }]
  })
})
</script>
