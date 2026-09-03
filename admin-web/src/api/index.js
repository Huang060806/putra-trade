import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearToken } from '../utils/auth'
import router from '../router'

const request = axios.create({ baseURL: '/', timeout: 15000 })

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) config.headers.Authorization = token
  return config
})

request.interceptors.response.use(
  (res) => {
    if (res.config.responseType === 'blob') return res
    const body = res.data
    if (body.code === 1) return body.data
    ElMessage.error(body.msg || '请求失败')
    return Promise.reject(new Error(body.msg))
  },
  (err) => {
    if (err.response?.status === 401) {
      clearToken()
      router.push('/login')
      ElMessage.error('登录已失效，请重新登录')
    } else {
      ElMessage.error(err.response?.data?.msg || '网络异常')
    }
    return Promise.reject(err)
  }
)

export default request
