const TOKEN_KEY = 'putra_member_token'
const USER_KEY = 'putra_member_info'
export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (t) => localStorage.setItem(TOKEN_KEY, t)
export const getUser = () => JSON.parse(localStorage.getItem(USER_KEY) || 'null')
export const setUser = (u) => localStorage.setItem(USER_KEY, JSON.stringify(u))
export const clearAuth = () => { localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY) }
