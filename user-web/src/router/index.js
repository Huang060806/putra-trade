import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/item/:id', component: () => import('../views/ItemDetail.vue') },
  { path: '/publish', component: () => import('../views/Publish.vue'), meta: { auth: true } },
  { path: '/want', component: () => import('../views/Want.vue'), meta: { auth: true } },
  { path: '/favorite', component: () => import('../views/Favorite.vue'), meta: { auth: true } },
  { path: '/orders', component: () => import('../views/Orders.vue'), meta: { auth: true } },
  { path: '/messages', component: () => import('../views/Messages.vue'), meta: { auth: true } },
  { path: '/mine', component: () => import('../views/Mine.vue'), meta: { auth: true } },
  { path: '/mine/items', component: () => import('../views/MyItems.vue'), meta: { auth: true } },
  { path: '/mine/profile', component: () => import('../views/Profile.vue'), meta: { auth: true } },
  { path: '/mine/spots', component: () => import('../views/MeetSpots.vue'), meta: { auth: true } }
]

const router = createRouter({ history: createWebHistory(), routes })
router.beforeEach((to) => {
  if (to.meta.auth && !getToken()) return '/login'
})
export default router
