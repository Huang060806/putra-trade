import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8081,
    proxy: {
      '/admin': 'http://localhost:8080',
      '/user': 'http://localhost:8080',
      '/upload': 'http://localhost:8080'
    }
  }
})
