<template>
  <div>
    <el-button type="primary" style="margin-bottom:12px" @click="open()">新增分类</el-button>
    <el-table :data="list">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="open(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" :title="form.id ? '编辑分类' : '新增分类'" width="380px">
      <el-form label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api'

const list = ref([])
const visible = ref(false)
const form = reactive({ id: null, name: '', sort: 0 })

const load = async () => { list.value = await request.get('/admin/category/list') }

const open = (row) => {
  Object.assign(form, row ? { id: row.id, name: row.name, sort: row.sort } : { id: null, name: '', sort: 0 })
  visible.value = true
}

const save = async () => {
  if (form.id) await request.put('/admin/category', form)
  else await request.post('/admin/category', form)
  ElMessage.success('已保存')
  visible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除分类「${row.name}」？`, '提示', { type: 'warning' })
  await request.delete(`/admin/category/${row.id}`)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>
