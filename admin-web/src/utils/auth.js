const KEY = 'putra_admin_token'
export const getToken = () => localStorage.getItem(KEY)
export const setToken = (t) => localStorage.setItem(KEY, t)
export const clearToken = () => localStorage.removeItem(KEY)
