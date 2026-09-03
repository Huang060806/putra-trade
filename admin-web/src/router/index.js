import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '数据看板' } },
      { path: 'items', component: () => import('../views/Items.vue'), meta: { title: '商品管理' } },
      { path: 'reports', component: () => import('../views/Reports.vue'), meta: { title: '举报审核' } },
      { path: 'members', component: () => import('../views/Members.vue'), meta: { title: '用户管理' } },
      { path: 'categories', component: () => import('../views/Categories.vue'), meta: { title: '分类管理' } },
      { path: 'words', component: () => import('../views/SensitiveWords.vue'), meta: { title: '敏感词库' } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to) => {
  if (to.path !== '/login' && !getToken()) return '/login'
})

export default router
