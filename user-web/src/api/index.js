import axios from 'axios'
import { showToast } from 'vant'
import { getToken, clearAuth } from '../utils/auth'
import router from '../router'

const request = axios.create({ baseURL: '/', timeout: 15000 })

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) config.headers.Authorization = token
  return config
})

request.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body.code === 1) return body.data
    showToast(body.msg || '请求失败')
    return Promise.reject(new Error(body.msg))
  },
  (err) => {
    if (err.response?.status === 401) {
      clearAuth()
      router.push('/login')
      showToast('请先登录')
    } else {
      showToast(err.response?.data?.msg || '网络异常')
    }
    return Promise.reject(err)
  }
)

export default request
