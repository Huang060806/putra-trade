<template>
  <van-tabbar route :model-value="active">
    <van-tabbar-item icon="home-o" to="/">首页</van-tabbar-item>
    <van-tabbar-item icon="plus" to="/publish">发布</van-tabbar-item>
    <van-tabbar-item icon="star-o" to="/want">想要</van-tabbar-item>
    <van-tabbar-item icon="chat-o" to="/messages">消息</van-tabbar-item>
    <van-tabbar-item icon="user-o" to="/mine">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
defineProps({ active: String })
</script>
