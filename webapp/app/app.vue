<script setup lang="ts">
const auth = useAuthStore()

onMounted(() => {
  if (!auth.details.id) {
    auth.saveDetailsJson(localStorage.getItem('user'))
  }
})
</script>

<template>
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>

<style>
.blur-enter-active,
.blur-leave-active {
  transition: all 0.2s;
}
.blur-enter-from,
.blur-leave-to {
  opacity: 0;
  filter: blur(1rem);
}
</style>
